package com.example.expensemanagement.presentation.insight_screen

import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.domain.models.Transaction
import com.example.expensemanagement.domain.usecase.read_database.GetAllFunds
import com.example.expensemanagement.domain.usecase.read_database.GetAllGroups
import com.example.expensemanagement.domain.usecase.read_database.GetAllParticipants
import com.example.expensemanagement.domain.usecase.read_database.GetFundByGroupId
import com.example.expensemanagement.domain.usecase.read_database.GetParticipantByFundId
import com.example.expensemanagement.domain.usecase.read_database.GetTransactionById
import com.example.expensemanagement.domain.usecase.read_database.GetTransactionByParticipant
import com.example.expensemanagement.domain.usecase.read_datastore.GetCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class InsightViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val getParticipantByFundId: GetParticipantByFundId,
    private val getAllGroups: GetAllGroups,
    private val getFundByGroupId: GetFundByGroupId,
    private val getTransactionByParticipant: GetTransactionByParticipant,
    private val getTransactionById: GetTransactionById
): ViewModel() {
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

    init {
        fetchSelectedCurrency()
        viewModelScope.launch(IO){
            getFundByGroupId(1).collect { listFundDto ->
                val funds = listFundDto?.map { fundDto ->
                    fundDto.toFund()
                } ?: emptyList()
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
    }
    fun getFormattedDate(date: Date): String {
        val dayOfWeek = DateFormat.format("EEE", date)
        val day = DateFormat.format("dd", date)
        val monthAbbr = DateFormat.format("MMM", date)

        return "$dayOfWeek $day, $monthAbbr"
    }
    fun getTransaction(participantId: Int){
        viewModelScope.launch(IO) {
            getTransactionByParticipant(participantId).collect{
                it.let{ listTransDto ->
                    val newTrans = listTransDto.map{ transDto ->
                        transDto.toTransaction()
                    }.reversed()
                    _transactionByParId.value = newTrans.groupBy { trans ->
                        getFormattedDate(trans.date)
                    }
                }
            }
        }
    }

    fun getTransById(transId: Int){
        viewModelScope.launch(IO){
            getTransactionById(transId).collect{
                _transactionById.value = it.toTransaction()
            }
        }
    }
    private fun fetchSelectedCurrency(){
        viewModelScope.launch(IO) {
            getCurrencyUseCase().collect{ selectedCurrency ->
                val currencyCode = selectedCurrency
                selectedCurrencyCode.value = currencyCode
            }
        }
    }

}