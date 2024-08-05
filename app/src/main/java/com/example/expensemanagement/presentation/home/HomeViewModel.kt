package com.example.expensemanagement.presentation.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanagement.common.Constants
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
import com.example.expensemanagement.domain.usecase.read_database.GetTransByFund
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
import com.example.expensemanagement.presentation.common.Category
import com.example.expensemanagement.presentation.common.TabButton
import com.example.expensemanagement.presentation.common.TabContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.Collator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
    private val updateParticipant: UpdateParticipant,
    private val getTransByFund: GetTransByFund,
    private val getParticipantByFundId: GetParticipantByFundId,
//    private val getTransactionByParticipant: GetTransactionByParticipant
) : ViewModel() {

    private val _groupById = MutableStateFlow<Group?>(null)
    val groupById: StateFlow<Group?> = _groupById

    private val _fundByGroupId = MutableStateFlow<List<Fund>>(emptyList())
    val fundByGroupId: StateFlow<List<Fund>> = _fundByGroupId

    private val _fundById = MutableStateFlow<Fund?>(null)
    val fundById: StateFlow<Fund?> = _fundById

    private val _parById = MutableStateFlow<Participant?>(null)
    val parById: StateFlow<Participant?> = _parById

    private val _parByFund = MutableStateFlow<List<Participant>>(emptyList())
    val parByFund: StateFlow<List<Participant>> = _parByFund

    private val _allParticipant = MutableStateFlow<List<Participant>>(emptyList())
    val allParticipant: StateFlow<List<Participant>> = _allParticipant

    private val _unpaidTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val unpaidTransactions: StateFlow<List<Transaction>> = _unpaidTransactions

    private val _transByFundAndPar = MutableStateFlow<List<Transaction>>(emptyList())
    val transByFundAndPar: StateFlow<List<Transaction>> = _transByFundAndPar

    private val _transByFund = MutableStateFlow<List<Transaction>>(emptyList())
    val transByFund: StateFlow<List<Transaction>> = _transByFund

    private val _transByPar = MutableStateFlow<List<Transaction>>(emptyList())
    val transByPar: StateFlow<List<Transaction>> = _transByPar

    private val _transWithPar = MutableStateFlow<List<Pair<Transaction, Participant>>>(emptyList())
    val transWithPar: StateFlow<List<Pair<Transaction, Participant>>> = _transWithPar

    private val _numberFund = MutableStateFlow(0)
    val numberFund: StateFlow<Int> = _numberFund

    private val _numberPar = MutableStateFlow(0)
    val numberPar: StateFlow<Int> = _numberPar

    private val _fundAndExpense = MutableStateFlow<List<Pair<Fund, Double>>>(emptyList())
    val fundAndExpense: StateFlow<List<Pair<Fund, Double>>> = _fundAndExpense

    private val _parAndBalance = MutableStateFlow<List<Pair<Participant, Double>>>(emptyList())
    val parAndBalance: StateFlow<List<Pair<Participant, Double>>> = _parAndBalance

    private val _parAndExpense = MutableStateFlow<List<Pair<Participant, Double>>>(emptyList())
    val parAndExpense: StateFlow<List<Pair<Participant, Double>>> = _parAndExpense


    var expense = MutableStateFlow(0.0)
        private set
    var income = MutableStateFlow(0.0)
        private set
    var balance = MutableStateFlow(0.0)
        private set
    var eraseState = MutableStateFlow(false)
        private set
    var groupName = MutableStateFlow(String())
        private set
    var fundName = MutableStateFlow(String())
        private set
    var parName = MutableStateFlow(String())
        private set
    var selectedCurrencyCode = MutableStateFlow(String())
        private set
    var tabFund = MutableStateFlow(TabContent.FUND)
        private set
    var tabPar = MutableStateFlow(TabContent.PARTICIPANT)
        private set
    var childCheckedStates = MutableStateFlow<List<Boolean>>(emptyList())
        private set
    var fundNameFieldValue by mutableStateOf(TextFieldValue(""))

    val collator = Collator.getInstance(Locale("vi", "VN"))
    init {
        fetchSelectedCurrency()
        viewModelScope.launch(IO) {

            getFundByGroupId(1).collect {
                it?.let { listFundDto ->
                    val listFund = listFundDto.map {
                        it.toFund()
                    }.sortedWith { fund1, fund2 ->
                        collator.compare(fund1.fundName, fund2.fundName)
                    }
                    val listPair = listFund.map { fund ->
                        val expense: Deferred<Double> = async { getExpenseByFund(fund.fundId) }
                        fund to expense.await()
                    }

                    _fundAndExpense.value = listPair
                }
            }
        }
        viewModelScope.launch(IO) {

            getAllParticipants().collect{

                it?.let { listParDto ->
                    val listPar = listParDto.map {
                        it.toParticipant()
                    }.sortedWith { par1, par2 ->
                        collator.compare(par1.participantName, par2.participantName)
                    }
                    val listPair = listPar.map { par ->
                        val expense: Deferred<Double> = async { getExpenseByPar(par.participantId) }
                        par to expense.await()
                    }
                    _parAndExpense.value = listPair
                }
            }
        }

    }


    fun getFormattedDate(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd").format(date)
    }

    private fun fetchSelectedCurrency() {
        viewModelScope.launch(IO) {
            getCurrencyUseCase().collect { selectedCurrency ->
                val currencyCode = selectedCurrency
                selectedCurrencyCode.value = currencyCode
            }
        }
    }


    fun getFundByGroup() {
        viewModelScope.launch(IO) {
            getFundByGroupId(1).collect { listFundDto ->
                val listFund = listFundDto.map {
                    it.toFund()
                }
                _fundByGroupId.value = listFund
                _numberFund.value = _fundByGroupId.value.size
            }
        }
    }

    fun getGroupByGroupId() {
        viewModelScope.launch(IO) {
            getGroupById(1).collect {
                _groupById.value = it.toGroup()
            }
        }
    }

    fun getFundByFundId(fundId: Int) {
        viewModelScope.launch(IO) {
            getFundById(fundId).collect {
                it?.let {
                    _fundById.value = it.toFund()
                }

            }
        }
    }

    fun getParByFundId(fundId: Int) {
        viewModelScope.launch(IO) {
            getParticipantByFundId(fundId).collect {
                _parByFund.value = it.map { parDto ->
                    parDto.toParticipant()
                }
            }
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
    suspend fun getBalanceByPar(parId: Int): Double {
        var totalExpense = 0.0
        var totalIncome = 0.0
        val listTrans = getTransactionByParticipant(parId).first()
        listTrans.let { listTransDto ->
            listTransDto.forEach { trans ->
                if (trans.transactionType == Constants.EXPENSE) {
                    totalExpense += trans.amount
                }else {
                    totalIncome += trans.amount
                }
            }
        }
        return (totalIncome - totalExpense)
    }
    suspend fun getExpenseByPar(parId: Int): Double {
        var totalExpense = 0.0
//        var totalIncome = 0.0
        val listTrans = getTransactionByParticipant(parId).first()
        listTrans.let { listTransDto ->
            listTransDto.forEach { trans ->
                if (trans.transactionType == Constants.EXPENSE) {
                    totalExpense += trans.amount
                }else {
//                    totalIncome += trans.amount
                }
            }
        }
        return (totalExpense)
    }

    fun getTransactionByFund(fundId: Int) {
        viewModelScope.launch(IO) {
            getTransByFund(fundId).collect {
                it?.let { listTransDto ->
                    _transByFund.value = listTransDto.map { transDto ->
                        transDto.toTransaction()
                    }.sortedByDescending { trans -> trans.date }
                }

            }
        }
    }

    fun getTransactionByPar(parId: Int) {
        viewModelScope.launch(IO) {
            getTransactionByParticipant(parId).collect {
                it?.let { listTransDto ->
                    _transByPar.value = listTransDto.map { transDto ->
                        transDto.toTransaction()
                    }.sortedByDescending { trans -> trans.date }
                }

            }
        }
    }

    fun getTransWithPar() {
        viewModelScope.launch(IO) {
            _transByFund.collect {
                it?.let { listTrans ->
                    val mappedList = listTrans.mapNotNull { trans ->
                        _parById.value = getParticipantById(trans.participantId).first().toParticipant()
                        _parById.value?.let {
                            trans to it
                        }
//                   trans to _parById!!.value
                    }

                    _transWithPar.value = mappedList
                }

            }
        }
    }

    fun getTransWithParByPar() {
        viewModelScope.launch(IO) {
            _transByPar.collect {
                it?.let { listTrans ->
                    val mappedList = listTrans.mapNotNull { trans ->
                        _parById.value = getParticipantById(trans.participantId).first().toParticipant()
                        _parById.value?.let {
                            trans to it
                        }
//                   trans to _parById!!.value
                    }

                    _transWithPar.value = mappedList
                }

            }
        }
    }

    fun getAllPars() {
        viewModelScope.launch(IO) {
            getAllParticipants().collect { listParDto ->
                val newListPar = listParDto.map { parDto ->
                    parDto.toParticipant()
                }
                _allParticipant.value = newListPar
                _numberPar.value = _allParticipant.value.size
            }
        }
    }


    fun getParById(parId: Int) {
        viewModelScope.launch(IO) {
            getParticipantById(parId).collect {
                it?.let {
                    _parById.value = it.toParticipant()
                }

            }
        }
    }

    fun insertFund(fundName: String, groupId: Int) {
        viewModelScope.launch(IO) {
            val newFundDto = FundDto(
                fundId = 0,
                fundName = fundName,
                groupId = 1
            )
            val fundId = insertNewFund(newFundDto)
            insertParticipantFund(1, fundId.toInt())
        }
    }

    fun insertParticipantFund(
        parId: Int,
        fundId: Int
    ) {
        viewModelScope.launch(IO) {
//            val newParticipantFund = ParticipantFundDto(
//                parFundId = 0,
//                participantId = parId,
//                fundId = fundId
//            )
//            insertNewParticipantFund(newParticipantFund)
//        }
            viewModelScope.launch(IO) {
//            selectedParticipants.forEach { participant ->
                val parFundDto =
                    getParFundByParAndFund(parId, fundId).firstOrNull()
                if (parFundDto == null) {
                    val newParFund = ParticipantFundDto(
                        parFundId = 0,
                        participantId = parId,
                        fundId = fundId
                    )
                    insertNewParticipantFund(newParFund)
                } else {
                }

//            }
            }
        }
    }

    fun insertParticipant(
        parName: String
    ) {
        viewModelScope.launch(IO) {
            val newParticipant = ParticipantDto(
                participantId = 0,
                participantName = parName,
            )
            insertNewParticipant(newParticipant)
        }
    }

    fun addParticipantToFund(selectedParticipants: List<Participant>, fundId: Int) {

        viewModelScope.launch(IO) {
            selectedParticipants.forEach { participant ->
                val parFundDto =
                    getParFundByParAndFund(participant.participantId, fundId).firstOrNull()
                if (parFundDto == null) {
                    val newParFund = ParticipantFundDto(
                        parFundId = 0,
                        participantId = participant.participantId,
                        fundId = fundId
                    )
                    insertNewParticipantFund(newParFund)
                } else {
                }

            }
        }
    }

    fun eraseParticipantToFund(parId: Int, fundId: Int) {
        viewModelScope.launch(IO) {
            getParFundByParAndFund(parId, fundId).collect { parFundDto ->
                if (parFundDto != null) {
                    eraseParFundById(parFundDto.parFundId)
                } else {
                }
            }
        }
    }

    fun setEraseState(_eraseState: Boolean) {
        eraseState.value = _eraseState
    }

    fun eraseParById(id: Int) {
        viewModelScope.launch(IO) {
            if (id == 1) {
                setEraseState(true)
            } else {
                eraseParticipantById(id)
            }
        }
    }

    fun eraseFundByFundId(id: Int) {
        viewModelScope.launch(IO) {
            if (id == 1) {
                setEraseState(true)
            } else {
                eraseFundById(id)
            }
        }

    }


    fun updateFundById(
        fundId: Int,
        fundName: String,
        groupId: Int
    ) {
        viewModelScope.launch(IO) {
            val updateFund = FundDto(
                fundId,
                fundName,
                groupId
            )
            updateFund(updateFund)
        }
    }

    fun insertParFund(
        fundId: Int,
        fundName: String,
        groupId: Int
    ) {
        viewModelScope.launch(IO) {
            val updateFund = FundDto(
                fundId,
                fundName,
                groupId
            )
            updateFund(updateFund)
        }
    }

    fun updateParticipantById(
        participantId: Int,
        participantName: String,
    ) {
        viewModelScope.launch(IO) {
            val updateParticipant = ParticipantDto(
                participantId,
                participantName,
            )
            updateParticipant(updateParticipant)
        }
    }

    fun setGroupName(name: String) {
        groupName.value = name
    }

    fun setFundName(name: String) {
        fundName.value = name
    }

    fun setParName(name: String) {
        parName.value = name
    }

    fun setTabFund(tab: TabContent) {
        tabFund.value = tab
    }

    fun setTabPar(tab: TabContent) {
        tabPar.value = tab
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

    fun getCategory(title: String): Category {
        var result: Category = Category.FOOD_DRINK
        Category.values().forEach {
            if (it.title == title)
                result = it
        }
        return result
    }
}

