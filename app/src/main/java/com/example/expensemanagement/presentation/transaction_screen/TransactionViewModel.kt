package com.example.expensemanagement.presentation.transaction_screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanagement.data.local.entity.FundDto
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.domain.usecase.read_database.GetAllFunds
import com.example.expensemanagement.domain.usecase.read_database.GetAllParticipants
import com.example.expensemanagement.domain.usecase.read_database.GetFundByGroupId
import com.example.expensemanagement.domain.usecase.read_database.GetParticipantByFundId
import com.example.expensemanagement.domain.usecase.write_database.InsertNewTransaction
import com.example.expensemanagement.presentation.common.Category
import com.example.expensemanagement.presentation.common.TabButton
import com.example.expensemanagement.presentation.common.TabTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val insertNewTransaction: InsertNewTransaction,
    private val getFundByGroupId: GetFundByGroupId,
    private val getParticipantByFundId: GetParticipantByFundId
): ViewModel(){
    var tabButton = MutableStateFlow(TabButton.EXPENSE)
        private set

    private val _fundByGroupId = MutableStateFlow<List<Fund>>(emptyList())
    val fundByGroupId: StateFlow<List<Fund>> = _fundByGroupId

    private val _participantByFundId = MutableStateFlow<Map<Int, List<Participant>>>(emptyMap())
    val participantByFundId: StateFlow<Map<Int, List<Participant>>> = _participantByFundId

    var initialParticipantName = MutableStateFlow(String())
        private set
    var _participantName = MutableStateFlow(initialParticipantName.value)
        private set
//    private val _participantName = MutableStateFlow(String())
    var category = MutableStateFlow(Category.FOOD_DRINK)
        private set
    var tabTransaction = MutableStateFlow(TabTransaction.INCOME_TAB)
    init {
        // Load the funds (you would fetch from repository/database here)
        viewModelScope.launch(IO) {
            getFundByGroupId(1).collect { listFundDto ->
                val funds = listFundDto?.map { fundDto ->
                    fundDto.toFund()
                } ?: emptyList()
                _fundByGroupId.value = funds
                Log.d("_fundByGroupId", _fundByGroupId.value.toString())

                _fundByGroupId.value.forEach { fund ->
                    val participantMap = mutableMapOf<Int, List<Participant>>()
                    getParticipantByFundId(fund.fundId).collect { listParDto ->
                        val participants = listParDto?.map { parDto ->
                            parDto.toParticipant()
                        } ?: emptyList()
                        participantMap[fund.fundId] = participants
                        _participantByFundId.value = participantMap
                        Log.d("_fundByGroupId", _participantByFundId.value.toString())
                        val participant = participantMap[fund.fundId] ?: emptyList()
                        initialParticipantName.value = participant.firstOrNull()?.participantName?: ""
                    }
                }
            }
        }
    }
    fun selectTabButton(button: TabButton) {
        tabButton.value = button
    }
    fun selectParticipant(participantName: String) {
        this._participantName.value = participantName
    }
    fun selectCategory(category: Category) {
        this.category.value = category
    }
    fun selectTabTransaction(button: TabTransaction){
        tabTransaction.value = button
    }
}

