package com.example.expensemanagement.presentation.transaction_screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.presentation.transaction_screen.TransactionViewModel

@Composable
fun ParDropdownMenu(
    onParSelected: (Participant) -> Unit,
    transactionViewModel: TransactionViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedPar by remember { mutableStateOf<Participant?>(null) }
    val participants by transactionViewModel.participantList.collectAsState()
    LaunchedEffect(participants) {
        if (participants.isNotEmpty()) {
            selectedPar = participants.first() // Chọn Fund đầu tiên làm mặc định
            onParSelected(selectedPar!!)
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { expanded = !expanded }) {
            Text(text = selectedPar?.participantName ?: "Select Fund")
        }
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            participants.forEach { fund ->
                DropdownMenuItem(
                    text = { Text(fund.participantName) },
                    onClick = {
                        selectedPar = fund
                        expanded = false
                        onParSelected(fund)
                    }
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun ParDropMenuPreview(){
    ParDropdownMenu(onParSelected = {})
}