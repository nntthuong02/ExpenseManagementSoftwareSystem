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


    init {


        Log.d("init PVM", _expenseByFund.value.toString())

        viewModelScope.launch(IO) {
            getFundByGroupId(1).collect { listFundDto ->
                listFundDto?.let {
                    val listFund = it.map { fundDto -> fundDto.toFund() }
                        .sortedWith { fund1, fund2 ->
                            collator.compare(fund1.fundName, fund2.fundName)
                        }

                    val fundExpensesDeferred = listFund.map { fund ->
                        async {
                            val expense = getExpenseByFund(fund.fundId)
                            fund to expense
                        }
                    }

                    val fundExpenses = fundExpensesDeferred.awaitAll()

                    _fundAndExpense.value = fundExpenses
                    Log.d("_fundAndExpense", _fundAndExpense.toString())
                }
            }
        }


//        viewModelScope.launch(IO) {
//
//            getFundByGroupId(1).collect {
//                it?.let { listFundDto ->
//                    Log.d("listFundDto", listFundDto.toString())
//                    val listFund = listFundDto.map {
//                        it.toFund()
//                    }
//                    var i = 0
//                    val fundExpensesDeferred = listFund.map { fund ->
//                        i++
//                        Log.d("i++", i.toString())
//                        Log.d("PVM fund2", fund.toString())
//                        val expense: Deferred<Double> = async { getExpenseByFund(fund.fundId) }
//                        Log.d("PVM expensefund2", expense.await().toString())
//                        fund to expense.await()
//                    }
//
//                    _fundAndExpense.value = fundExpensesDeferred
//                    Log.d("_fundAndExpense", _fundAndExpense.toString())
//                }
//            }
//        }
//        viewModelScope.launch(IO) {
//            val participants = getAllParticipants().first()
//
//            val participantExpenses = participants.map { participant ->
//                async {
//                    val fundExpenses = mutableListOf<Double>()
//                    getFundByPar(participant.participantId).collect { funds ->
//                        funds?.forEach { fund ->
//                            val countDeferred = async { countParsInFund(fund.fundId) }
//                            val expenseDeferred = async { getExpenseByFund(fund.fundId) }
//                            val count = countDeferred.await()
//                            val expense = expenseDeferred.await()
//                            fundExpenses.add(expense / count)
//                        }
//                    }
//                    fundExpenses.sum()
//                }
//            }
//
//            val results = participantExpenses.awaitAll().zip(participants) { expense, participant ->
//                participant.toParticipant() to expense
//            }
//
//            _parAndExpense.value = results
//        }

        viewModelScope.launch(IO) {
            val listPar = getAllParticipants().first().map { it.toParticipant() }
                .sortedWith { par1, par2 ->
                    collator.compare(par1.participantName, par2.participantName)
                }
            Log.d("listPar", listPar.toString())

//            var expensePar = 0.0
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
                            fundExpenses.add(expense / count)

                    }
                    Log.d("expenseParDeferred", expenseParDeferred.toString())
                    Log.d("fundExpenses", fundExpenses.toString())
                    fundExpenses.sum() - expenseParDeferred.await()

                }
            }
            val results = participantExpenses.awaitAll().zip(listPar) { expense, participant ->
                participant to expense
            }
            _parAndExpense.value = results
            Log.d("_parAndExpense", _parAndExpense.toString())
        }

//
//        viewModelScope.launch(IO) {
////            getAllParticipants().first()
//            getAllParticipants().collect {
//                it?.let { listParDto ->
//                    val listPar = listParDto.map {
//                        it.toParticipant()
//                    }
//                    Log.d("PVM listPar", listPar.toString())
//                    val pair = listPar.map { par ->
//                        Log.d("PVM par", par.toString())
//                        var expense = 0.0
//                        var amountFund = 0.0
//                        getFundByPar(par.participantId).collect { listFundDto ->
//                            Log.d("listFundDto.isNotEmpty()", listFundDto.toString())
//                            if (listFundDto.isNotEmpty()) {
//                                Log.d("listFundDto.isNotEmpty()", "ok")
//                                listFundDto.forEach { fundDto ->
//                                    val count = countParsInFund(fundDto.fundId)
//                                    Log.d("listFundDto.isNotEmpty()1", count.toString())
//                                    val expenseFund = getExpenseByFund(fundDto.fundId)
//                                    Log.d("listFundDto.isNotEmpty()2", expenseFund.toString())
//                                    if (expenseFund != null && count != null) {
//                                        amountFund += (expenseFund / count)
//                                    }
//
//                                    Log.d("listFundDto.isNotEmpty()3", amountFund.toString())
//                                }
//                                Log.d("PVM2", "ok")
//                            }
//                            Log.d("PVM3", "ok")
//                        }
//                        Log.d("PVM4", "ok")
//                        Log.d("PVM expensepar", expense.toString())
//                        Log.d("PVM amountfund", amountFund.toString())
//                        val amountPar = getExpenseByPar(par.participantId)
//                        expense = amountFund - amountPar
//                        par to expense
//                    }
//                    _parAndAmount.value = pair
//                }
//            }
//        }

//        viewModelScope.launch(IO) {
//            Log.d("getExpenseByFundtest", getExpenseByFund(1).toString())
//            getFundByPar(1).collect{
//                Log.d("getFundByPar", it.toString())
//            }
//
//        }

    }

    fun setTabBar(tab: TabContent) {
        tabBar.value = tab
    }

    fun convertDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd").format(date)
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

            // In ra kết quả để kiểm tra
            Log.d("getPaidTransactionCountsByDate", result.toString())
        }
    }
    }

    // Biến LiveData để lưu trữ danh sách các pair nếu cần

    fun undoPay() {
        //Sửa hàm này và Entity: Transaction.dateOfEntry thành Date
        //Tránh trường hợp undo cùng ngày và xoá cả lần thanh toán trước đó
        viewModelScope.launch(IO) {
            val transactions = getAllTransactions().first() // Lấy tất cả các Transaction
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
            Log.d("listTransDto", listTransDto.toString())
            listTransDto.forEach { trans ->
                if (trans.transactionType == Constants.EXPENSE) {
                    Log.d("Constants.EXPENSE", Constants.EXPENSE)
                    Log.d("trans.transactionType", trans.transactionType)
                    totalExpense += trans.amount
                    Log.d("getTransByFund PVM", totalExpense.toString())
                }
            }
        }
        Log.d("getTransByFund PVM2", totalExpense.toString())
        Log.d("getExpenseByFund2", totalExpense.toString())
        return totalExpense
    }

    suspend fun getExpenseByPar(parId: Int): Double {
        var totalExpense = 0.0
        val listTrans = getTransactionByParticipant(parId).first()
        listTrans.let { listTransDto ->
            Log.d("listTransDto", listTransDto.toString())
            listTransDto.forEach { trans ->
                if (trans.transactionType == Constants.EXPENSE) {
                    totalExpense += trans.amount
                }
            }
        }
        return totalExpense
    }

    suspend fun countParsInFund(fundId: Int): Int {
//        var count = 0
        val listParDto = getParticipantByFundId(fundId).first()
//        listParDto.forEach { parDto ->
//
//            count++
//
//        }
        return listParDto.size
    }


}