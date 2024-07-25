//package com.example.expensemanagement.presentation.home
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.expensemanagement.domain.models.Transaction
//import com.example.expensemanagement.domain.usecase.read_database.GetAllFunds
//import com.example.expensemanagement.domain.usecase.read_database.GetAllGroups
//import com.example.expensemanagement.domain.usecase.read_database.GetAllParticipants
//import com.example.expensemanagement.domain.usecase.read_database.GetFundByGroupId
//import com.example.expensemanagement.domain.usecase.read_database.GetFundById
//import com.example.expensemanagement.domain.usecase.read_database.GetParticipantByFundId
//import com.example.expensemanagement.domain.usecase.read_database.GetParticipantById
//import com.example.expensemanagement.domain.usecase.read_database.GetTransactionById
//import com.example.expensemanagement.domain.usecase.read_database.GetTransactionByParticipant
//import com.example.expensemanagement.domain.usecase.read_datastore.GetCurrencyUseCase
//import com.example.expensemanagement.domain.usecase.write_database.InsertNewFund
//import com.example.expensemanagement.domain.usecase.write_database.InsertNewParticipant
//import com.example.expensemanagement.domain.usecase.write_database.InsertNewParticipantFund
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Dispatchers.IO
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val insertNewFund: InsertNewFund,
//    private val insertNewParticipantFund: InsertNewParticipantFund,
//    private val insertNewParticipant: InsertNewParticipant,
//    private val getAllParticipants: GetAllParticipants,
//    private val getCurrencyUseCase: GetCurrencyUseCase,
//    private val getAllGroups: GetAllGroups,
//    private val getFundByGroupId: GetFundByGroupId,
//    private val getTransactionByParticipant: GetTransactionByParticipant,
//    private val getTransactionById: GetTransactionById,
//    private val getFundById: GetFundById,
//    private val getParticipantById: GetParticipantById
//): ViewModel() {
//    var selectedCurrencyCode = MutableStateFlow(String())
//        private set
//
//    private val _groupById = MutableStateFlow<Group?>(null)
//    val groupById: StateFlow<Group?> = _groupById
//
//    private val _fundByGroupId = MutableStateFlow<List<Fund>>(emptyList())
//    val fundByGroupId: StateFlow<List<Fund>> = _fundByGroupId
//
//    private val _allParticipant = MutableStateFlow<List<Participant>>(emptyList())
//    val allParticipant: StateFlow<List<Participant>> = _allParticipant
//
//    private val _unpaidTransactions = MutableStateFlow<List<Transaction>>(emptyList())
//    val unpaidTransactions: StateFlow<List<Transaction>> = _unpaidTransactions
//
//    init {
//
//    }
//
//    private fun fetchSelectedCurrency(){
//        viewModelScope.launch(IO){
//            getCurrencyUseCase().collect{
//                val currencyCode = selectedCurrency
//            }
//        }
//    }
//}