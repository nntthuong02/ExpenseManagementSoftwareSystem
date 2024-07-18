package com.example.expensemanagement.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Dummy data model
data class Group(val id: Int, var name: String)
data class Fund(val id: Int, var name: String)
data class Participant(val id: Int, var name: String)

@Composable
fun HomeScreen() {
    // Dummy data
    val group = remember { mutableStateOf(Group(1, "Group A")) }
    val fundList = remember { mutableStateOf(listOf(Fund(1, "Fund 1"), Fund(2, "Fund 2"))) }
    val participantList = remember { mutableStateOf(listOf(Participant(1, "Participant 1"), Participant(2, "Participant 2"))) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Edit Group name
        EditableEntity(
            entity = group.value,
            onValueChange = { newValue -> group.value = group.value.copy(name = newValue) },
            label = "Group"
        )

        // Edit Fund names
        fundList.value.forEach { fund ->
            EditableEntity(
                entity = fund,
                onValueChange = { newValue ->
                    val updatedList = fundList.value.toMutableList()
                    updatedList.find { it.id == fund.id }?.name = newValue
                    fundList.value = updatedList
                },
                label = "Fund ${fund.id}"
            )
        }

        // Edit Participant names
        participantList.value.forEach { participant ->
            EditableEntity(
                entity = participant,
                onValueChange = { newValue ->
                    val updatedList = participantList.value.toMutableList()
                    updatedList.find { it.id == participant.id }?.name = newValue
                    participantList.value = updatedList
                },
                label = "Participant ${participant.id}"
            )
        }

        // Add Fund button
        Button(
            onClick = { fundList.value = fundList.value + Fund(fundList.value.size + 1, "New Fund") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Fund")
        }

        // Add Participant button
        Button(
            onClick = { participantList.value = participantList.value + Participant(participantList.value.size + 1, "New Participant") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Participant")
        }
    }
}

@Composable
fun EditableEntity(entity: Any, onValueChange: (String) -> Unit, label: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            OutlinedTextField(
                value = (entity as? Group)?.name ?: (entity as? Fund)?.name ?: (entity as? Participant)?.name ?: "",
                onValueChange = { onValueChange(it) },
                label = { Text("Enter $label name") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { /* Handle done action if needed */ }),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}