package com.example.expensemanagement.presentation.transaction_screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.presentation.transaction_screen.TransactionViewModel

@Composable
fun ParDropdownMenu(
    onParSelected: (Participant) -> Unit,
    participants: List<Participant>,
    transactionViewModel: TransactionViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedPar by remember { mutableStateOf<Participant?>(null) }
//    val participants by transactionViewModel.participantList.collectAsState()
    LaunchedEffect(participants) {
        if (participants.isNotEmpty()) {
            selectedPar = participants.first() // Chọn Par đầu tiên làm mặc định
            onParSelected(selectedPar!!)
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { expanded = !expanded }) {
            Box(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = selectedPar?.participantName ?: "Select Participant",
                    modifier = Modifier.align(Alignment.Center)
                )
                Icon(
                    Icons.Default.KeyboardArrowDown, contentDescription = "Localized description",
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

        }
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            participants.forEach { par ->
                DropdownMenuItem(
                    text = { Text(par.participantName) },
                    onClick = {
                        selectedPar = par
                        expanded = false
                        onParSelected(par)
                    }
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun ParDropMenuPreview() {
    val fakeParticipants = listOf(
        Participant(1, participantName = "Participant 1"),
        Participant(2, participantName = "Participant 2"),
        Participant(3, participantName = "Participant 3")
    )
    ParDropdownMenu(onParSelected = {}, fakeParticipants)
}