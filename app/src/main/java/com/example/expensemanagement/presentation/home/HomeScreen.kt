package com.example.expensemanagement.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.presentation.home.component.EditNameEntity
import com.example.expensemanagement.presentation.home.component.EntityItem
import com.example.expensemanagement.presentation.navigation.Route
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


// Dummy data model
data class GroupData(val id: Int, var name: String)
data class FundData(val id: Int, var name: String)
data class ParticipantData(val id: Int, var name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val fundData = Fund(1, "Thuong",  1)

    val groupName by remember { mutableStateOf(homeViewModel.groupName) }
    val groupNameFieldValue = TextFieldValue(groupName.collectAsState().value)

    val parName by remember { mutableStateOf(homeViewModel.parName) }
    val parNameFieldValue = TextFieldValue(parName.collectAsState().value)

    val groupById by homeViewModel.groupById.collectAsState()
    val fundsByGroup by homeViewModel.fundByGroupId.collectAsState()
    val fundById by homeViewModel.fundById.collectAsState()
    val allPar by homeViewModel.allParticipant.collectAsState()
    val parById by homeViewModel.parById.collectAsState()
    val currency by homeViewModel.selectedCurrencyCode.collectAsState()
    val numberFund by homeViewModel.numberFund.collectAsState()
    val numberPar by homeViewModel.numberPar.collectAsState()



    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Dummy data
//    val group = remember { mutableStateOf(GroupData(1, "GroupData A")) }
    val fundList = remember { mutableStateOf(listOf(FundData(1, "Fund 1"), FundData(2, "Fund 2"))) }
    val participantList = remember { mutableStateOf(listOf(ParticipantData(1, "Participant 1"), ParticipantData(2, "Participant 2"))) }
    var initialGroupName = remember { String() }
    LaunchedEffect(Unit){
        coroutineScope {
            launch {
                homeViewModel.getGroupByGroupId()
            }
            launch {
                homeViewModel.getFundByGroup()
            }
            launch {
                homeViewModel.getAllPars()
            }

        }
    }
    groupById?.let{group ->
        initialGroupName = group.groupName
        LaunchedEffect(initialGroupName){
            homeViewModel.setGroupName(initialGroupName)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Edit Group name
        EditNameEntity(
            "Group",
            groupNameFieldValue.text,
            onNameChange = {
                homeViewModel.setGroupName(it)
            },
            onSaveClick = {
                /*Todo*/
            }
        )
        Spacer(modifier = Modifier.padding(5.dp))
        // List fund
        EntityItem(
            nameEntity = "Fund",
            number = fundsByGroup.size.toString(),
            itemOnClick = {
                          navController.navigate(Route.ListFundScreen.route)
                          },
            backgroundColor = Color.DarkGray.copy(alpha= 0.1f),
            surfaceColor = Color.Blue
        )
        Spacer(modifier = Modifier.padding(5.dp))
        //List participant
        EntityItem(
            nameEntity = "Participant",
            number = fundsByGroup.size.toString(),
            itemOnClick = {
                          navController.navigate(Route.ListParticipantScreen.route)
                          },
            backgroundColor = Color.DarkGray.copy(alpha= 0.1f),
            surfaceColor = Color.Green
        )
        Spacer(modifier = Modifier.padding(5.dp))
        SnackbarHost(hostState = snackbarHostState)


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
                value = (entity as? GroupData)?.name ?: (entity as? FundData)?.name ?: (entity as? ParticipantData)?.name ?: "",
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
    HomeScreen(navController = rememberNavController())
}