package com.example.expensemanagement.presentation.transaction_screen

import androidx.lifecycle.ViewModel
import com.example.expensemanagement.domain.usecase.read_database.GetAllFunds
import com.example.expensemanagement.domain.usecase.read_database.GetAllParticipants
import com.example.expensemanagement.domain.usecase.write_database.InsertNewTransaction
import com.example.expensemanagement.presentation.common.TabButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val insertNewTransaction: InsertNewTransaction,
    private val getAllFunds: GetAllFunds,

): ViewModel(){
    var tabButton = MutableStateFlow(TabButton.EXPENSE)
        private set

    fun selectTabButton(button: TabButton) {
        tabButton.value = button
    }
}