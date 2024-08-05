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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
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

    private val _fundByGroup = MutableStateFlow<List<Fund>>(emptyList())
    val fundByGroup: StateFlow<List<Fund>> = _fundByGroup

    private val _parByFund = MutableStateFlow<Map<Int, List<Participant>>>(emptyMap())
    val parByFund: StateFlow<Map<Int, List<Participant>>> = _parByFund

    private val _parList = MutableStateFlow<List<Participant>>(emptyList())
    val parList: StateFlow<List<Participant>> = _parList

    private val _transList = MutableStateFlow<List<Transaction>>(emptyList())
    val transList: StateFlow<List<Transaction>> = _transList

    private val _transByFundAndPar = MutableStateFlow<List<Transaction>>(emptyList())
    val transByFundAndPar: StateFlow<List<Transaction>> = _transByFundAndPar

    private val _transByFund = MutableStateFlow<List<Transaction>>(emptyList())
    val transByFund: StateFlow<List<Transaction>> = _transByFund

    private val _fundAndExpense = MutableStateFlow<List<Pair<Fund, Double>>>(emptyList())
    val fundAndExpense: StateFlow<List<Pair<Fund, Double>>> = _fundAndExpense

    //    private val _selectedDate = MutableStateFlow(Date())
//    val selectedDate: StateFlow<Date> = _selectedDate
    private val _fundById = MutableStateFlow<Fund?>(null)
    val fundById: StateFlow<Fund?> = _fundById

    private val _parById = MutableStateFlow<Participant?>(null)
    val parById: StateFlow<Participant?> = _parById

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

    init {
        fetchSelectedCurrency()
        viewModelScope.launch(IO) {
            getFundByGroupId(1).collect { listFundDto ->
                val funds = listFundDto?.map { fundDto ->
                    fundDto.toFund()
                }?.sortedBy { it.fundName } ?: emptyList()
                _fundByGroup.value = funds
                Log.d("_fundByGroupId", _fundByGroup.value.toString())

                _fundByGroup.value.forEach { fund ->
                    val participantMap = mutableMapOf<Int, List<Participant>>()
                    getParticipantByFundId(fund.fundId).collect { listParDto ->
                        val participants = listParDto?.map { parDto ->
                            parDto.toParticipant()
                        } ?: emptyList()
                        participantMap[fund.fundId] = participants
                        _parByFund.value = participantMap
                        Log.d("_fundByGroup", _parByFund.value.toString())
                    }
                }
            }
        }

//        viewModelScope.launch(IO) {
//            getFundByGroupId(1).collect {
//                it?.let { listFundDto ->
//                    Log.d("listFundDto", listFundDto.toString())
//                    val listFund = listFundDto.map {
//                        it.toFund()
//                    }
//                    val pair = listFund.map { fund ->
//                        Log.d("PVM fund", fund.toString())
//                        var expense = getExpenseByFund(fund.fundId)
//                        Log.d("PVM expensefund", expense.toString())
//                        fund to expense
//                    }
//
//                    _fundAndExpense.value = pair
//                    Log.d("_fundAndExpense.value", _fundAndExpense.value.toString())
//                }
//            }
//        }
        viewModelScope.launch(IO) {

            getFundByGroupId(1).collect {
                it?.let { listFundDto ->
                    Log.d("listFundDto", listFundDto.toString())
                    val listFund = listFundDto.map {
                        it.toFund()
                    }
                    var i = 0
                    val listPair = listFund.map { fund ->
                        i++
                        Log.d("i++", i.toString())
                        Log.d("PVM fund2", fund.toString())
                        val expense: Deferred<Double> = async { getExpenseByFund(fund.fundId) }
                        Log.d("PVM expensefund2", expense.await().toString())
                        fund to expense.await()
                    }

                    _fundAndExpense.value = listPair
                    Log.d("_fundAndExpense", _fundAndExpense.toString())
                }
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
                    _transByFundAndPar.value = newListTrans
                }
            }
        }
    }

    fun getTransactionByFund(fundId: Int) {
        viewModelScope.launch(IO) {
            getTransByFund(fundId).collect {listTransDto ->
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
            Log.d("eraseTransactionById", "ok")
        }
    }

    //    fun getTransById(transId: Int){
//        viewModelScope.launch(IO){
//            getTransactionById(transId).collect{
//                _transactionById.value = it.toTransaction()
//                Log.d("getTransById", _transactionById.value.toString())
//            }
//        }
//    }
    fun getTransById(transId: Int) {
        viewModelScope.launch(IO) {
            getTransactionById(transId).collect { transactionDto ->
                transactionDto?.let {
                    _transactionById.value = it.toTransaction()
                    Log.d("getTransById", _transactionById.value.toString())
                } ?: run {
                    Log.e("getTransById", "TransactionDto is null for transactionId: $transId")
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("InsightViewModel onCleared", "ViewModel is being cleared")
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