package com.example.expensemanagement.presentation.payment_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.domain.models.Transaction
import com.example.expensemanagement.domain.usecase.read_database.GetAllParticipants
import com.example.expensemanagement.domain.usecase.read_database.GetAllTransactions
import com.example.expensemanagement.domain.usecase.read_database.GetFundByGroupId
import com.example.expensemanagement.domain.usecase.read_database.GetFundById
import com.example.expensemanagement.domain.usecase.read_database.GetFundByPar
import com.example.expensemanagement.domain.usecase.read_database.GetParFundByParAndFund
import com.example.expensemanagement.domain.usecase.read_database.GetParticipantByFundId
import com.example.expensemanagement.domain.usecase.read_database.GetParticipantById
import com.example.expensemanagement.domain.usecase.read_database.GetTransByFund
import com.example.expensemanagement.domain.usecase.read_database.GetTransactionByParticipant
import com.example.expensemanagement.domain.usecase.write_database.UndoPayment
import com.example.expensemanagement.domain.usecase.write_database.UpdatePayTransactions
import com.example.expensemanagement.presentation.common.TabContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import java.text.Collator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

//import kotlinx.coroutines.*
@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val getFundByGroupId: GetFundByGroupId,
    private val getAllParticipants: GetAllParticipants,
    private val getTransByFund: GetTransByFund,
    private val getTransactionByParticipant: GetTransactionByParticipant,
    private val getParFundByParAndFund: GetParFundByParAndFund,
    private val getFundById: GetFundById,
    private val getParticipantById: GetParticipantById,
    private val getParticipantByFundId: GetParticipantByFundId,
    private val updatePayTransactions: UpdatePayTransactions,
    private val getFundByPar: GetFundByPar,
    private val getAllTransactions: GetAllTransactions,
    private val undoPayment: UndoPayment
) : ViewModel() {
    private val _fundByGroupId = MutableStateFlow<List<Fund>>(emptyList())
    val fundByGroupId: StateFlow<List<Fund>> = _fundByGroupId

    private val _allParticipant = MutableStateFlow<List<Participant>>(emptyList())
    val allParticipant: StateFlow<List<Participant>> = _allParticipant

    private val _transByFundAndPar = MutableStateFlow<List<Transaction>>(emptyList())
    val transByFundAndPar: StateFlow<List<Transaction>> = _transByFundAndPar

    private val _transByFund = MutableStateFlow<List<Transaction>>(emptyList())
    val transByFund: StateFlow<List<Transaction>> = _transByFund

    private val _transByPar = MutableStateFlow<List<Transaction>>(emptyList())
    val transByPar: StateFlow<List<Transaction>> = _transByPar

    private val _parAndExpense = MutableStateFlow<List<Pair<Participant, Double>>>(emptyList())
    val parAndExpense: StateFlow<List<Pair<Participant, Double>>> = _parAndExpense

    private val _fundAndExpense = MutableStateFlow<List<Pair<Fund, Double>>>(emptyList())
    val fundAndExpense: StateFlow<List<Pair<Fund, Double>>> = _fundAndExpense

    private val _expenseByFund = MutableStateFlow(0.0)
    val expenseByFund: StateFlow<Double> = _expenseByFund

    private val _expenseFund = MutableStateFlow(0.0)
    val expenseFund: StateFlow<Double> = _expenseFund

    private val _expenseByPar = MutableStateFlow(0.0)
    val expenseByPar: StateFlow<Double> = _expenseByPar

    private val _countParsInFund = MutableStateFlow(0)
    val countParsInFund: StateFlow<Int> = _countParsInFund

    private val _transactionsByDate = MutableStateFlow<List<Pair<String, Int>>>(emptyList())
    val transactionsByDate: StateFlow<List<Pair<String, Int>>> = _transactionsByDate
    var tabBar = MutableStateFlow(TabContent.PAYMENT)
        private set

    val collator = Collator.getInstance(Locale("vi", "VN"))
    fun getFormattedDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd").format(date)
    }

    fun formatDouble(value: Double): Double {
        return String.format(Locale.US, "%.1f", value).toDouble()
    }

    init {



        viewModelScope.launch(IO) {
            getFundByGroupId(1).collect { listFundDto ->
                listFundDto?.let {
                    val listFund = it.map { fundDto -> fundDto.toFund() }
                        .sortedWith { fund1, fund2 ->
                            collator.compare(fund1.fundName, fund2.fundName)
                        }

                    val fundExpensesDeferred = listFund.map { fund ->
                        async {
                            val expense = formatDouble(getExpenseByFund(fund.fundId))
                            fund to expense
                        }
                    }

                    val fundExpenses = fundExpensesDeferred.awaitAll()

                    _fundAndExpense.value = fundExpenses
                }
            }
        }


        viewModelScope.launch(IO) {
            val listPar = getAllParticipants().first().map { it.toParticipant() }
                .sortedWith { par1, par2 ->
                    collator.compare(par1.participantName, par2.participantName)
                }

            val participantExpenses = listPar.map { participant ->
                async {
                    val expenseParDeferred: Deferred<Double> = async { getExpenseByPar(participant.participantId) }
                    val fundExpenses = mutableListOf<Double>()
                    val funds = getFundByPar(participant.participantId).firstOrNull() ?: emptyList()
                    funds.forEach{fundDto ->
                            val countDeferred = async { countParsInFund(fundDto.fundId) }
                            val expenseDeferred = async { getExpenseByFund(fundDto.fundId) }
                            val count = countDeferred.await()
                            val expense = expenseDeferred.await()
                            fundExpenses.add(formatDouble(expense / count))

                    }
                    fundExpenses.sum() - formatDouble(expenseParDeferred.await())

                }
            }
            val results = participantExpenses.awaitAll().zip(listPar) { expense, participant ->
                participant to expense
            }
            _parAndExpense.value = results
        }

    }

    fun setTabBar(tab: TabContent) {
        tabBar.value = tab
    }

    fun convertDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
    }
    fun paymentExpense(
        dateOfEntry: String
    ){
        viewModelScope.launch(IO){
            updatePayTransactions(dateOfEntry)
        }
    }

    fun getPaidTransactionCountsByDate() {
        viewModelScope.launch(IO) {
            getAllTransactions().collect { transactionList ->

            val paidTransactions = transactionList.filter { it.isPaid }


            val dateCounts = paidTransactions
//                Map<String, List<TransactionDto>>
                .groupBy { trans -> trans.dateOfEntry }
//                Map<String, Int>
                .mapValues { (_, transactions) -> transactions.size }

            val result = dateCounts.map { (date, count) -> date to count }


            _transactionsByDate.value = result


        }
    }
    }



    fun undoPay() {
        //Đã sửa thêm ngày giờ cho thời gian thanh toán
        viewModelScope.launch(IO) {
            val transactions = getAllTransactions().first()
            val latestDate = transactions.filter { it.dateOfEntry != null }
                .maxByOrNull { it.dateOfEntry }
                ?.dateOfEntry
            if (latestDate != null){
            undoPayment(time = latestDate)
            }
        }
    }


    suspend fun getExpenseByFund(fundId: Int): Double {
        var totalExpense = 0.0
        val listTrans = getTransByFund(fundId).first()
        listTrans.let { listTransDto ->
            listTransDto.forEach { trans ->
                if (trans.transactionType == Constants.EXPENSE && trans.isPaid == false) {
                    totalExpense += trans.amount
                }
            }
        }
        return totalExpense
    }

    suspend fun getExpenseByPar(parId: Int): Double {
        var totalExpense = 0.0
        val listTrans = getTransactionByParticipant(parId).first()
        listTrans.let { listTransDto ->
            listTransDto.forEach { trans ->
                if (trans.transactionType == Constants.EXPENSE && trans.isPaid == false) {
                    totalExpense += trans.amount
                }
            }
        }
        return totalExpense
    }

    suspend fun countParsInFund(fundId: Int): Int {
//        var count = 0
        val listParDto = getParticipantByFundId(fundId).first()
        return listParDto.size
    }


}