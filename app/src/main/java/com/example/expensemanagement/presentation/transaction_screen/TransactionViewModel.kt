package com.example.expensemanagement.presentation.transaction_screen

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.data.local.entity.FundDto
import com.example.expensemanagement.data.local.entity.GroupDto
import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.data.local.entity.ParticipantFundDto
import com.example.expensemanagement.data.local.entity.TransactionDto
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.domain.models.Transaction
import com.example.expensemanagement.domain.usecase.GetDateUseCase
import com.example.expensemanagement.domain.usecase.GetFormattedDateUseCase
import com.example.expensemanagement.domain.usecase.read_database.GetAllTransactions
import com.example.expensemanagement.domain.usecase.read_database.GetFundByGroupId
import com.example.expensemanagement.domain.usecase.read_database.GetFundById
import com.example.expensemanagement.domain.usecase.read_database.GetParFundByParAndFund
import com.example.expensemanagement.domain.usecase.read_database.GetParticipantByFundId
import com.example.expensemanagement.domain.usecase.read_database.GetParticipantById
import com.example.expensemanagement.domain.usecase.read_database.GetTransactionById
import com.example.expensemanagement.domain.usecase.read_datastore.GetCurrencyUseCase
import com.example.expensemanagement.domain.usecase.write_database.InsertNewFund
import com.example.expensemanagement.domain.usecase.write_database.InsertNewGroup
import com.example.expensemanagement.domain.usecase.write_database.InsertNewParticipant
import com.example.expensemanagement.domain.usecase.write_database.InsertNewParticipantFund
import com.example.expensemanagement.domain.usecase.write_database.InsertNewTransaction
import com.example.expensemanagement.domain.usecase.write_database.UpdateFund
import com.example.expensemanagement.domain.usecase.write_database.UpdateParFund
import com.example.expensemanagement.domain.usecase.write_database.UpdateParticipant
import com.example.expensemanagement.domain.usecase.write_database.UpdateTransaction
import com.example.expensemanagement.domain.usecase.write_database.UpdateTransactionDetails
import com.example.expensemanagement.presentation.common.Category
import com.example.expensemanagement.presentation.common.TabButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val insertNewTransaction: InsertNewTransaction,
    private val getFundByGroupId: GetFundByGroupId,
    private val getParticipantByFundId: GetParticipantByFundId,
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val getDateUseCase: GetDateUseCase,
    private val getFormattedDateUseCase: GetFormattedDateUseCase,
    private val getParticipantById: GetParticipantById,
    private val insertNewParticipant: InsertNewParticipant,
    private val getAllTransactions: GetAllTransactions,
    private val insertNewGroup: InsertNewGroup,
    private val updateFund: UpdateFund,
    private val updateParFund: UpdateParFund,
    private val updateParticipant: UpdateParticipant,
    private val insertNewParticipantFund: InsertNewParticipantFund,
    private val insertNewFund: InsertNewFund,
    private val getFundById: GetFundById,
    private val updateTransactionDetails: UpdateTransactionDetails,
    private val getTransactionById: GetTransactionById,
    private val getParFundByParAndFund: GetParFundByParAndFund

) : ViewModel() {
    var tabButton = MutableStateFlow(TabButton.EXPENSE)
        private set

    //chỉnh sửa biến thành List<FundDto>(hoac nguoc lai) và sửa các hàm dùng biến này
    //Giảm bớt biến không cần thiết
    private val _fundByGroupId = MutableStateFlow<List<Fund>>(emptyList())
    val fundByGroupId: StateFlow<List<Fund>> = _fundByGroupId


    private val _fundByGroupIdDto = MutableStateFlow<List<FundDto>>(emptyList())
    val fundByGroupIdDto: StateFlow<List<FundDto>> = _fundByGroupIdDto

    private val _participantByFundId = MutableStateFlow<Map<Int, List<Participant>>>(emptyMap())
    val participantByFundId: StateFlow<Map<Int, List<Participant>>> = _participantByFundId


    //

    //
//    var initialParticipantName = MutableStateFlow(String())
//        private set
//    var _participantName = MutableStateFlow(initialParticipantName.value)
//        private set
//    private val _participantName = MutableStateFlow(String())
    private val _participantList = MutableStateFlow<List<Participant>>(emptyList())
    val participantList: StateFlow<List<Participant>> = _participantList

    var selectedParticipant = MutableStateFlow<Participant?>(null)
        private set

    //
    var selectedFund = MutableStateFlow<Fund?>(null)
        private set

    //
    var category = MutableStateFlow(Category.FOOD_DRINK)
        private set
    var transactionTitle = MutableStateFlow(String())
        private set
    var transactionAmount = MutableStateFlow(String())
        private set
    var selectedCurrencyCode = MutableStateFlow(String())
        private set
    var selectedFundId = MutableStateFlow(1)
        private set

    var currentTime = MutableStateFlow(Calendar.getInstance().time)
        private set
    var formattedDate = MutableStateFlow(String())
        private set
    var date = MutableStateFlow(String())
        private set
    var isPaidT = MutableStateFlow(false)
        private set
    var selectedDate = MutableStateFlow(Date())
        private set

    fun selectDate(date: Date) {
        selectedDate.value = date
    }

    fun convertDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd").format(date)
    }

    init {
//        val currentDate = getDateUseCase()
//        formattedDate.value = getFormattedDateUseCase(currentTime.value)
//        date.value = currentDate
        fetchSelectedCurrency()

        // Load the funds (you would fetch from repository/database here)
        viewModelScope.launch(IO) {

            val participantMap = mutableMapOf<Int, List<Participant>>()
            val listFund = getFunds()
            _fundByGroupId.value = listFund
            if (_fundByGroupId.value.isNotEmpty()) {
                _fundByGroupId.value.forEach { fund ->
                    // Thực hiện các hành động khác
                    val listPar = getParByFund(fund.fundId)
                    _participantList.value = listPar
                    participantMap[fund.fundId] = _participantList.value
                }
            } else {
            }
            _participantByFundId.value = participantMap
        }
        viewModelScope.launch {
            getAllTransactions().collect {
            }
        }
    }

suspend fun getFunds(): List<Fund> {
    return withContext(IO) {
        val listFundDto = getFundByGroupId(1).firstOrNull() ?: emptyList()
        listFundDto.map { it.toFund() }
    }
}

    suspend fun getParByFund(fundId: Int): List<Participant>{
        return withContext(IO){
            val listParDto = getParticipantByFundId(fundId).firstOrNull()?: emptyList()
            listParDto.map { it.toParticipant() }
        }
    }

    fun selectTabButton(button: TabButton) {
        tabButton.value = button
    }

    fun selectParticipant(participant: Participant) {
        selectedParticipant.value = participant
    }

    fun selectFund(fund: Fund) {
        selectedFund.value = fund
    }

    fun selectCategory(category: Category) {
        this.category.value = category
    }

    fun setTransactionTitle(title: String) {
        transactionTitle.value = title
    }

    fun setTransaction(amount: String) {
        transactionAmount.value = amount
    }

    fun setFundId(fundId: Int) {
        selectedFundId.value = fundId
    }

    fun setCurrentTime(time: Date) {
        currentTime.value = time
    }

    fun setIsPaid(isPaid: Boolean) {
        isPaidT.value = isPaid
    }

    private fun fetchSelectedCurrency() {
        viewModelScope.launch(IO) {
            getCurrencyUseCase().collect { selectedCurrency ->
                selectedCurrencyCode.value = selectedCurrency
            }
        }
    }

    fun Transaction.toTransactionDto(): TransactionDto = TransactionDto(
        transactionId = 0,
        date = date,
        dateOfEntry = dateOfEntry,
        amount = amount,
        category = category,
        transactionType = transactionType,
        title = title,
        participantId = participantId,
        isPaid = isPaid,
        fundId = fundId
    )

    fun updateExpenseFund(
        fundId: Int,
        fundName: String,
    ) {
        viewModelScope.launch(IO) {
            val updateFund = FundDto(
                fundId,
                fundName,
                1,
            )
            updateFund(updateFund)
        }
    }
    fun formatDouble(value: Double): Double {
        return String.format("%.1f", value).toDouble()
    }


    fun addNewTransaction(
//        transactionId: Int,
        selectedDate: Date,
        date: String,
        amount: Double,
        category: String,
        transactionType: String,
        transactionTitle: String,
        isPaid: Boolean,
        selectedParId: Int,
        selectedFundId: Int,
        navigateBack: () -> Unit
    ) {
        viewModelScope.launch(IO) {
            val parFundDto = getParFundByParAndFund(selectedParId, selectedFundId).first()
            val newTransaction = TransactionDto(
                transactionId = 0,
                transactionTitle,
                selectedDate,
                date,
                formatDouble(amount),
                category,
                isPaid,
                transactionType,
                selectedParId,
                selectedFundId
            )

            if (parFundDto != null) {
                val parFundUpdate = ParticipantFundDto(
                    parFundDto.parFundId,
                    selectedParId,
                    selectedFundId
                )
                insertNewParticipantFund(parFundUpdate)
            } else {
                val newParFundDto = ParticipantFundDto(
                    parFundId = 0,
                    participantId = selectedParId,
                    fundId = selectedFundId
                )
                insertNewParticipantFund(newParFundDto)
            }
            insertNewTransaction(newTransaction)
//            withContext(Main) {
//                navigateBack()
//            }
        }
    }

    fun updateTransactionById(
        transactionId: Int,
        selectedDate: Date,
        amount: Double,
        category: String,
        transactionType: String,
        transactionTitle: String,
        selectedParId: Int,
        selectedFundId: Int,
        initialFundId: Int,
        navigateBack: () -> Unit
    ) {
        viewModelScope.launch(IO) {
            val transDto = getTransactionById(transactionId).first()

            val parId: Int = transDto.participantId
            val parFundDto = getParFundByParAndFund(parId, initialFundId).first()

            val parFundUpdate = ParticipantFundDto(
                parFundDto.parFundId,
                selectedParId,
                selectedFundId
            )
            updateParFund(parFundUpdate)
            updateTransactionDetails(
                transactionId,
                transactionTitle,
                selectedDate,
                formatDouble(amount),
                category,
                transactionType,
                selectedParId,
                selectedFundId
            )
//            withContext(Main) {
//                navigateBack()
//            }
        }
    }

    fun createEntity() {
        viewModelScope.launch(IO) {
            insertNewGroup.invoke(GroupDto(1, "My Group"))
            insertNewFund.invoke(FundDto(1, "My Fund", 1))
            insertNewParticipant.invoke((ParticipantDto(1, "I")))
            insertNewParticipantFund.invoke(ParticipantFundDto(1, 1, 1))
        }
    }
}

