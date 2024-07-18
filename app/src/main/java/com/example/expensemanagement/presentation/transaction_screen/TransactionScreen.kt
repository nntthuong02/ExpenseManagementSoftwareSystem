package com.example.expensemanagement.presentation.transaction_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expensemanagement.presentation.transaction_screen.component.FundDropdownMenu
import com.example.expensemanagement.presentation.transaction_screen.component.ParticipantTag
import com.example.expensemanagement.presentation.transaction_screen.component.TabTransactionType

@Composable
fun TransactionScreen() {
//    Text("TransactionScreen")
    Column(modifier = Modifier.fillMaxSize()) {
        TabTransactionType()
        Spacer(modifier = Modifier.padding(10.dp))
        FundDropdownMenu(onFundSelected = {})
        ParticipantTag(participantName = "Thuong")
    }

}