package com.example.expensemanagement.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanagement.data.local.entity.FundDto
import com.example.expensemanagement.data.local.entity.GroupDto
import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.data.local.entity.ParticipantFundDto
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Group
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.domain.models.Transaction
import com.example.expensemanagement.domain.usecase.read_database.GetAllFunds
import com.example.expensemanagement.domain.usecase.read_database.GetAllGroups
import com.example.expensemanagement.domain.usecase.read_database.GetAllParticipants
import com.example.expensemanagement.domain.usecase.read_database.GetFundByGroupId
import com.example.expensemanagement.domain.usecase.read_database.GetFundById
import com.example.expensemanagement.domain.usecase.read_database.GetGroupById
import com.example.expensemanagement.domain.usecase.read_database.GetParFundByParAndFund
import com.example.expensemanagement.domain.usecase.read_database.GetParticipantByFundId
import com.example.expensemanagement.domain.usecase.read_database.GetParticipantById
import com.example.expensemanagement.domain.usecase.read_database.GetTransactionById
import com.example.expensemanagement.domain.usecase.read_database.GetTransactionByParticipant
import com.example.expensemanagement.domain.usecase.read_datastore.GetCurrencyUseCase
import com.example.expensemanagement.domain.usecase.write_database.EraseFundById
import com.example.expensemanagement.domain.usecase.write_database.EraseParFundById
import com.example.expensemanagement.domain.usecase.write_database.EraseParticipantById
import com.example.expensemanagement.domain.usecase.write_database.InsertNewFund
import com.example.expensemanagement.domain.usecase.write_database.InsertNewParticipant
import com.example.expensemanagement.domain.usecase.write_database.InsertNewParticipantFund
import com.example.expensemanagement.domain.usecase.write_database.UpdateFund
import com.example.expensemanagement.domain.usecase.write_database.UpdateGroup
import com.example.expensemanagement.domain.usecase.write_database.UpdateParFund
import com.example.expensemanagement.domain.usecase.write_database.UpdateParticipant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val insertNewFund: InsertNewFund,
    private val insertNewParticipantFund: InsertNewParticipantFund,
    private val insertNewParticipant: InsertNewParticipant,
    private val getAllParticipants: GetAllParticipants,
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val getAllGroups: GetAllGroups,
    private val getFundByGroupId: GetFundByGroupId,
    private val getTransactionByParticipant: GetTransactionByParticipant,
    private val getTransactionById: GetTransactionById,
    private val getParFundByParAndFund: GetParFundByParAndFund,
    private val getFundById: GetFundById,
    private val getParticipantById: GetParticipantById,
    private val getGroupById: GetGroupById,
    private val eraseFundById: EraseFundById,
    private val eraseParFundById: EraseParFundById,
    private val eraseParticipantById: EraseParticipantById,
    private val updateGroup: UpdateGroup,
    private val updateFund: UpdateFund,
    private val updateParFund: UpdateParFund,
    private val updateParticipant: UpdateParticipant
): ViewModel() {
    var selectedCurrencyCode = MutableStateFlow(String())
        private set

    private val _groupById = MutableStateFlow<Group?>(null)
    val groupById: StateFlow<Group?> = _groupById

    private val _fundByGroupId = MutableStateFlow<List<Fund>>(emptyList())
    val fundByGroupId: StateFlow<List<Fund>> = _fundByGroupId

    private val _fundById = MutableStateFlow<Fund?>(null)
    val fundById: StateFlow<Fund?> = _fundById

    private val _parById = MutableStateFlow<Participant?>(null)
    val parById: StateFlow<Participant?> = _parById

    private val _allParticipant = MutableStateFlow<List<Participant>>(emptyList())
    val allParticipant: StateFlow<List<Participant>> = _allParticipant

    private val _unpaidTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val unpaidTransactions: StateFlow<List<Transaction>> = _unpaidTransactions

    private val _transByFundAndPar = MutableStateFlow<List<Transaction>>(emptyList())
    val transByFundAndPar: StateFlow<List<Transaction>> = _transByFundAndPar

    private val _transByFund = MutableStateFlow<List<Transaction>>(emptyList())
    val transByFund: StateFlow<List<Transaction>> = _transByFund

    var eraseState = MutableStateFlow(false)
        private set
    var groupName = MutableStateFlow(String())
        private set
    var fundName = MutableStateFlow(String())
        private set
    var parName = MutableStateFlow(String())
        private set
    init {
        fetchSelectedCurrency()

    }

    fun getFormattedDate(date: Date): String{
        return SimpleDateFormat("yyyy-MM-dd").format(date)
    }
    private fun fetchSelectedCurrency(){
        viewModelScope.launch(IO){
            getCurrencyUseCase().collect{selectedCurrency ->
                val currencyCode = selectedCurrency
                selectedCurrencyCode.value = currencyCode
            }
        }
    }


    fun getFundByGroup(){
        viewModelScope.launch(IO){
            getFundByGroupId(1).collect{
                _fundByGroupId.value
            }
        }
    }
    fun getGroupByGroupId(){
        viewModelScope.launch(IO){
            getGroupById(1).collect{
                _groupById.value = it.toGroup()
            }
        }
    }
    fun getFundByFundId(fundId: Int){
        viewModelScope.launch(IO){
            getFundById(fundId).collect{
                _fundById.value = it.toFund()
            }
        }
    }

    fun getAllPars(){
        viewModelScope.launch(IO){
            getAllParticipants().collect{listParDto ->
                val newListPar = listParDto.map { parDto ->
                    parDto.toParticipant()
                }
                _allParticipant.value = newListPar
            }
        }
    }
    fun getParById(parId: Int){
        viewModelScope.launch(IO){
            getParticipantById(parId).collect{
                _parById.value = it.toParticipant()
            }
        }
    }

    fun insertFund(fundName: String, groupId: Int){
        viewModelScope.launch(IO){
            val newFundDto = FundDto(
                fundId = 0,
                fundName = fundName,
                totalAmount = 0.0,
                groupId = 1
            )
            insertNewFund(newFundDto)
        }
    }
    fun insertParticipantFund(
        parId: Int,
        fundId: Int
    ){
        viewModelScope.launch(IO){
            val newParticipantFund = ParticipantFundDto(
                parFundId = 0,
                participantId = parId,
                fundId = fundId
            )
            insertNewParticipantFund(newParticipantFund)
        }
    }
    fun insertParticipant(
        parName: String
    ){
        viewModelScope.launch(IO){
            val newParticipant = ParticipantDto(
                participantId = 0,
                participantName = parName,
                income = 0.0,
                expense = 0.0,
                balance = 0.0
            )
            insertNewParticipant(newParticipant)
        }
    }
    fun addParticipantToFund(parId: Int, fundId: Int){
        viewModelScope.launch(IO){
            getParFundByParAndFund(parId, fundId).collect{ parFundDto ->
                if(parFundDto == null){
                    val newParFund = ParticipantFundDto(
                        parFundId = 0,
                        participantId = parId,
                        fundId = fundId
                    )
                    insertNewParticipantFund(newParFund)
                }else {
                    Log.d("addParticipantToFund", "no addParticipantToFund")
                }
            }
        }
    }
    fun eraseParticipantToFund(parId: Int, fundId: Int){
        viewModelScope.launch(IO){
            getParFundByParAndFund(parId, fundId).collect{ parFundDto ->
                if(parFundDto != null){
                    eraseParFundById(parFundDto.parFundId)
                }else {
                    Log.d("eraseParticipantToFund", "no eraseParticipantToFund")
                }
            }
        }
    }
    fun setEraseState(_eraseState: Boolean){
        eraseState.value = _eraseState
    }
    fun eraseParById(id: Int){
        viewModelScope.launch(IO){
            if(id == 1){
                setEraseState(true)
            }else {
                eraseParticipantById(id)
            }
        }
    }

    fun eraseFundByFundId(id: Int){
        viewModelScope.launch(IO){
            if(id == 1){
                setEraseState(true)
            }else {
                eraseFundById(id)
            }
        }
    }

    fun updateGroupById(
        groupId: Int,
        groupName: String
    ){
        viewModelScope.launch(IO){
            val updateGroup = GroupDto(
                groupId,
                groupName,
            )
            updateGroup(updateGroup)
        }
    }
    fun updateFundById(
        fundId: Int,
        fundName: String,
        totalAmount: Double,
        groupId: Int
    ){
        viewModelScope.launch(IO){
            val updateFund = FundDto(
                fundId,
                fundName,
                totalAmount,
                groupId
            )
            updateFund(updateFund)
        }
    }
    fun updateParticipantById(
        participantId: Int,
        participantName: String,
        income: Double,
        expense: Double,
        balance: Double
    ){
        viewModelScope.launch(IO){
            val updateParticipant = ParticipantDto(
                participantId,
                participantName,
                income,
                expense,
                balance
            )
            updateParticipant(updateParticipant)
        }
    }

}