package com.example.expensemanagement.presentation.insight_screen

import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.domain.models.Transaction
import com.example.expensemanagement.domain.usecase.read_database.GetAllFunds
import com.example.expensemanagement.domain.usecase.read_database.GetAllGroups
import com.example.expensemanagement.domain.usecase.read_database.GetAllParticipants
import com.example.expensemanagement.domain.usecase.read_database.GetFundByGroupId
import com.example.expensemanagement.domain.usecase.read_database.GetFundById
import com.example.expensemanagement.domain.usecase.read_database.GetParFundByParAndFund
import com.example.expensemanagement.domain.usecase.read_database.GetParticipantByFundId
import com.example.expensemanagement.domain.usecase.read_database.GetParticipantById
import com.example.expensemanagement.domain.usecase.read_database.GetTransByFund
import com.example.expensemanagement.domain.usecase.read_database.GetTransByFundAndPar
import com.example.expensemanagement.domain.usecase.read_database.GetTransactionById
import com.example.expensemanagement.domain.usecase.read_database.GetTransactionByParticipant
import com.example.expensemanagement.domain.usecase.read_datastore.GetCurrencyUseCase
import com.example.expensemanagement.domain.usecase.write_database.EraseTransactionById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Collator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class InsightViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val getParticipantByFundId: GetParticipantByFundId,
    private val getAllGroups: GetAllGroups,
    private val getFundByGroupId: GetFundByGroupId,
    private val getTransactionByParticipant: GetTransactionByParticipant,
    private val getTransactionById: GetTransactionById,
    private val getFundById: GetFundById,
    private val getParticipantById: GetParticipantById,
    private val eraseTransactionById: EraseTransactionById,
    private val getTransByFundAndPar: GetTransByFundAndPar,
    private val getTransByFund: GetTransByFund
) : ViewModel() {
    //    var transactions = MutableStateFlow(mapOf<String, List<Transaction>>())
//        private set
//    var parByFund = MutableStateFlow(emptyList<Participant>())
//        private set
//    var fundByGroup = MutableStateFlow(emptyList<Fund>())
//        private set
    var selectedCurrencyCode = MutableStateFlow(String())
        private set
    private val _transactionByParId = MutableStateFlow<Map<String, List<Transaction>>>(emptyMap())
    val transactionByParId: StateFlow<Map<String, List<Transaction>>> = _transactionByParId

    private val _transactionById = MutableStateFlow<Transaction?>(null)
    val transactionById: StateFlow<Transaction?> = _transactionById

//    private val _fundByGroup = MutableStateFlow<List<Fund>>(emptyList())
//    val fundByGroup: StateFlow<List<Fund>> = _fundByGroup

    private val _parByFund = MutableStateFlow<Map<Int, List<Participant>>>(emptyMap())
    val parByFund: StateFlow<Map<Int, List<Participant>>> = _parByFund

    private val _parList = MutableStateFlow<List<Participant>>(emptyList())
    val parList: StateFlow<List<Participant>> = _parList

    private val _transList = MutableStateFlow<List<Transaction>>(emptyList())
    val transList: StateFlow<List<Transaction>> = _transList

    private val _transByFundAndPar = MutableStateFlow<Map<String, List<Transaction>>>(emptyMap())
    val transByFundAndPar: StateFlow<Map<String, List<Transaction>>> = _transByFundAndPar

    private val _transByFund = MutableStateFlow<List<Transaction>>(emptyList())
    val transByFund: StateFlow<List<Transaction>> = _transByFund


    //    private val _selectedDate = MutableStateFlow(Date())
//    val selectedDate: StateFlow<Date> = _selectedDate
    private val _fundById = MutableStateFlow<Fund?>(null)
    val fundById: StateFlow<Fund?> = _fundById

    private val _parById = MutableStateFlow<Participant?>(null)
    val parById: StateFlow<Participant?> = _parById

    private val _fundAndExpense = MutableStateFlow<List<Pair<Fund, Double>>>(emptyList())
    val fundAndExpense: StateFlow<List<Pair<Fund, Double>>> = _fundAndExpense
    private val _parAndExpense = MutableStateFlow<List<Pair<Participant, Double>>>(emptyList())
    val parAndExpense: StateFlow<List<Pair<Participant, Double>>> = _parAndExpense

    private val _expenseByFund = MutableStateFlow(0.0)
    val expenseByFund: StateFlow<Double> = _expenseByFund

    var selectedDate = MutableStateFlow(Date())
        private set

    var fundAmount = MutableStateFlow(0.0)
        private set
    var expense = MutableStateFlow(0.0)
        private set
    var income = MutableStateFlow(0.0)
        private set

    var balance = MutableStateFlow(0.0)
        private set

    val collator = Collator.getInstance(Locale("vi", "VN"))

    init {
        fetchSelectedCurrency()

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
                    val participantMap = mutableMapOf<Int, List<Participant>>()
                    fundExpenses.forEach { (fund, _) ->

                        val listParDto = getParticipantByFundId(fund.fundId).first()
                        participantMap[fund.fundId] = listParDto?.map { parDto ->
                            parDto.toParticipant()
                        } ?: emptyList()
                    }
                    _parByFund.value = participantMap

                }
            }
        }
    }

    suspend fun getExpenseByParAndFund(fundId: Int, parId: Int): Double {
        var totalExpense = 0.0
        val listTrans = getTransByFundAndPar(fundId, parId).first()
        listTrans.let { listTransDto ->
            listTransDto.forEach { trans ->
                if (trans.transactionType == Constants.EXPENSE) {
                    totalExpense += trans.amount
                }
            }
        }
        return totalExpense
    }

    fun expenseByParAndFund(fundId: Int, participants: List<Participant>) {
        viewModelScope.launch(IO) {
            participants?.sortedWith { par1, par2 ->
                collator.compare(par1.participantName, par2.participantName)
            }
            val parFundExpenseDeferred = participants.map { par ->
                async {
                    val expense = getExpenseByParAndFund(fundId, par.participantId)
                    par to expense
                }
            }
            val parFundExpense = parFundExpenseDeferred.awaitAll()
            _parAndExpense.value = parFundExpense
        }
    }

    suspend fun getExpenseByFund(fundId: Int): Double {
        var totalExpense = 0.0
        val listTrans = getTransByFund(fundId).first()
        listTrans.let { listTransDto ->
            listTransDto.forEach { trans ->
                if (trans.transactionType == Constants.EXPENSE) {
                    totalExpense += trans.amount
                }
            }
        }
        return totalExpense
    }


    //    fun getFormattedDate(date: Date): String {
//        val dayOfWeek = DateFormat.format("EEE", date)
//        val day = DateFormat.format("dd", date)
//        val monthAbbr = DateFormat.format("MMM", date)
//
//        return "$dayOfWeek $day, $monthAbbr"
//    }
    fun getFormattedDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd").format(date)
    }

    fun setFundAmount(amount: Double) {
        fundAmount.value = amount
    }

    fun setExpense(amount: Double) {
        expense.value = amount
    }

    fun setIncome(amount: Double) {
        income.value = amount
    }

    fun setBalance(amount: Double) {
        balance.value = amount
    }

    fun selectDate(date: Date) {
        selectedDate.value = date
    }

    fun getTransaction(participantId: Int) {
        viewModelScope.launch(IO) {
            getTransactionByParticipant(participantId).collect {
                it.let { listTransDto ->
                    val newTrans = listTransDto.map { transDto ->
                        transDto.toTransaction()
                    }.sortedByDescending { trans -> trans.date }
                    _transList.value = newTrans
                    _transactionByParId.value = newTrans.groupBy { trans ->
                        getFormattedDate(trans.date)
                    }
                }
            }
        }
    }

    fun getTransactionByParAndFund(fundId: Int, parId: Int) {
        viewModelScope.launch(IO) {
            getTransByFundAndPar(fundId, parId).collect {
                it.let { listTransDto ->
                    val newListTrans = listTransDto.map { transDto ->
                        transDto.toTransaction()
                    }.sortedByDescending { trans -> trans.date }
                    _transByFundAndPar.value = newListTrans.groupBy { trans ->
                        getFormattedDate(trans.date)
                    }
                }
            }
        }
    }

    fun getTransactionByFund(fundId: Int) {
        viewModelScope.launch(IO) {
            getTransByFund(fundId).collect { listTransDto ->
                listTransDto.let { listTransDto ->
                    _transByFund.value = listTransDto.map { transDto ->
                        transDto.toTransaction()
                    }.sortedByDescending { trans -> trans.date }
                }
            }
        }
    }

    fun eraseTransaction(
        transId: Int,
    ) {
        viewModelScope.launch(IO) {
            eraseTransactionById(transId)
        }
    }

    fun getTransById(transId: Int) {
        viewModelScope.launch(IO) {
            getTransactionById(transId).collect { transactionDto ->
                transactionDto?.let {
                    _transactionById.value = it.toTransaction()
                } ?: run {
                    Log.e("getTransById", "TransactionDto is null for transactionId: $transId")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun getParByFund(fundId: Int) {
        viewModelScope.launch(IO) {
            getParticipantByFundId(fundId).collect { participantDtoList ->
                _parList.value = participantDtoList.map { it.toParticipant() }
            }
        }
    }

    fun getFundByFundId(fundId: Int) {
        viewModelScope.launch(IO) {
            getFundById(fundId).collect {
                _fundById.value = it.toFund()
            }
        }
    }

    fun getParById(parId: Int) {
        viewModelScope.launch(IO) {
            getParticipantById(parId).collect {
                _parById.value = it.toParticipant()
            }
        }
    }

    private fun fetchSelectedCurrency() {
        viewModelScope.launch(IO) {
            getCurrencyUseCase().collect { selectedCurrency ->
                val currencyCode = selectedCurrency
                selectedCurrencyCode.value = currencyCode
            }
        }
    }

}