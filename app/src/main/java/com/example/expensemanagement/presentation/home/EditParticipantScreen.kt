package com.example.expensemanagement.presentation.home

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.domain.models.Transaction
import com.example.expensemanagement.presentation.common.TabContent
import com.example.expensemanagement.presentation.home.component.EditNameEntity
import com.example.expensemanagement.presentation.home.component.TabBar
import com.example.expensemanagement.presentation.home.component.TransItem
import com.example.expensemanagement.presentation.insight_screen.component.getCategory
import com.example.expensemanagement.presentation.navigation.Route
import kotlinx.coroutines.launch


@Composable
fun EditParticipantScreen(
    parId: Int,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val selectedTab by homeViewModel.tabPar.collectAsState()
    val parById by homeViewModel.parById.collectAsState()
    val allPars by homeViewModel.allParticipant.collectAsState()
    val transByPar by homeViewModel.transByPar.collectAsState()
    val currencyCode by homeViewModel.selectedCurrencyCode.collectAsState()
    val transWithPar by homeViewModel.transWithPar.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val parTitle by remember { mutableStateOf(homeViewModel.parName) }
    val parNameFieldValue = TextFieldValue(parTitle.collectAsState().value)

    LaunchedEffect(parId) {
        homeViewModel.apply {
            getParById(parId)
            getTransactionByPar(parId)
            getAllPars()
            getTransWithParByPar()
        }
        Log.d("edit par screen", parId.toString())
    }
    Log.d("LaunchedEffect(transByPar)", transByPar.toString())
    Log.d("LaunchedEffect(transWithPar)", transWithPar.toString())
    if (transByPar != null) {
        var sum = 0.0
        transByPar.forEach { trans ->
            if (trans.transactionType == Constants.EXPENSE) {
                sum += trans.amount
            }
        }
        homeViewModel.setExpense(sum)
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Row with buttons to switch between tabs
        TabBar(entityTab = TabContent.PARTICIPANT, selectedTab = selectedTab) { tabContent ->
            homeViewModel.setTabPar(tabContent)
        }

        // Content below changes based on the content variable
//        content()
        AnimatedContent(targetState = selectedTab) { targetTab ->
            when (targetTab) {
                TabContent.PARTICIPANT -> ParticipantContent(
                    parNameFieldValue = parNameFieldValue,
                    snackbarHostState = snackbarHostState,
                    onChange = {
                        homeViewModel.setParName(it)
                        Log.d("onChange", it)
                        Log.d("onChange par name", homeViewModel.parName.value)
                    },
                    onSave = {
                        Log.d("parNameFieldValue", parNameFieldValue.text)
                        if (parNameFieldValue.text.isEmpty()) {
                            // Hiển thị Snackbar thông báo lỗi
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please enter name")
                            }
                        } else {
                            coroutineScope.launch {
                                homeViewModel.apply {
                                    updateParticipantById(parId, parName.value)
                                    Log.d("parNameFieldValue", parNameFieldValue.text)
                                    navController.navigateUp()
                                    navController.navigate("${Route.EditParticipantScreen.route}/${parById!!.participantId}")
                                }
                            }
                        }
                    },
                    allParticipant = allPars,
//                    saveParticipant = { listChecked ->
//                        val selectedParticipants = allPars.filterIndexed { index, _ ->
//                            listChecked[index]
//                        }
//                        coroutineScope.launch{
//                            homeViewModel.addParticipantToFund(selectedParticipants, parId)
//                            Toast.makeText(context, "save participant successfully", Toast.LENGTH_SHORT).show()
//                        }
//
//                    },
                    deletePar = {
                        if (parId != 1){
                            coroutineScope.launch {
                                homeViewModel.eraseParById(parId)
                            }
//                        navController.navigate("${Route.ListFundScreen.route}")

                            navController.navigateUp()
                            Toast.makeText(context, "erase successfully", Toast.LENGTH_SHORT).show()
                        } else{
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Cannot delete default participant!")
//                            Toast.makeText(context, "Cannot delete default fund!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )

                else -> {
                    TransactionContent(
                        currencyCode = currencyCode,
                        transByFund = transByPar,
                        transWithPar = transWithPar,
                        onItemClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun ParticipantContent(
    parNameFieldValue: TextFieldValue,
    snackbarHostState: SnackbarHostState,
    onChange: (String) -> Unit,
    onSave: () -> Unit,
    deletePar: () -> Unit,
    allParticipant: List<Participant>,
//    saveParticipant: (List<Boolean>) -> Unit,
) {
    val childCheckedStates = remember { mutableStateListOf<Boolean>() }
    LaunchedEffect(allParticipant.size) {
        childCheckedStates.addAll(List(allParticipant.size) { false })
    }
    val parentState = when {
        childCheckedStates.all { it } -> ToggleableState.On
        childCheckedStates.none { it } -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }
//    Column(
//        modifier = Modifier
//            .fillMaxWidth(),
//        verticalArrangement = Arrangement.SpaceBetween
//    ) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        EditNameEntity(
            nameEntity = "Participant",
            name = parNameFieldValue.text,
            onNameChange = onChange,
            onSaveClick = onSave
        )
//        {
//            onSave()
//        }

//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(start = 5.dp, end = 5.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            // Parent TriStateCheckbox
//            Box(
//                modifier = Modifier
//                    .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
////                        .background(Color.LightGray) // Màu nền cho văn bản
//                    .padding(0.dp) // Khoảng cách giữa văn bản và viền nền
//                    .fillMaxWidth()
//            ) {
//                Text(
//                    text = "Add paritcipant",
//                    modifier = Modifier
//
//                        .align(Alignment.Center)
//                        .padding(8.dp), // Tạo khoảng cách giữa văn bản và đường viền
//                    style = MaterialTheme.typography.titleSmall,
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.onSurface,
//
//                    )
//            }
////                Text("Add participant")
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                Text("Select all        ")
//                TriStateCheckbox(
//                    state = parentState,
//                    onClick = {
//                        val newState = parentState != ToggleableState.On
//                        childCheckedStates.forEachIndexed { index, _ ->
//                            childCheckedStates[index] = newState
//                        }
//                    }
//                )
//            }
//            LazyColumn {
//                itemsIndexed(childCheckedStates) { index, checked ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                    ) {
//                        Text("${allParticipant[index].participantName}     ")
//                        Checkbox(
//                            checked = checked,
//                            onCheckedChange = { isChecked ->
//                                childCheckedStates[index] = isChecked
//                            }
//                        )
//                    }
//                }
//            }
//
//        }
//            }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SnackbarHost(hostState = snackbarHostState)
//            Button(
//                modifier = Modifier.fillMaxWidth(),
//                onClick = { saveParticipant(childCheckedStates) }
//            ) {
//                Text(text = "Save participant")
//            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = deletePar
            ) {
                Text(text = "Delete participant")
            }
        }

    }
//    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Transaction2Content(
    currencyCode: String,
    transByPar: List<Transaction>,
    transWithPar: List<Pair<Transaction, Participant>>,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
//    val parId by homeViewModel.parById.collectAsState()
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.padding(
            top = 5.dp
        )
    ) {
        Log.d("transByPar Trans", transByPar.toString())
        if (transByPar == null) {
            Box(
                modifier = Modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
//                        .background(Color.LightGray) // Màu nền cho văn bản
                    .padding(0.dp) // Khoảng cách giữa văn bản và viền nền
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Not Transaction",
                    modifier = Modifier

                        .align(Alignment.Center)
                        .padding(8.dp), // Tạo khoảng cách giữa văn bản và đường viền
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,

                    )
            }

        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 5.dp,
                    top = 5.dp,
                    end = 5.dp
                )
            ) {
//                item {
//                    Text(
//                        text = "Not Transaction",
//                        textAlign = TextAlign.Center,
//                        color = MaterialTheme.colorScheme.onBackground,
//                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Normal),
//                    )
//
//                }
                Log.d("test ParticipantDetail", "test1")
                itemsIndexed(transWithPar) { index, (trans, participant) ->

//                    coroutineScope.launch {
//                        homeViewModel.getParById(trans.participantId)
//                    }
                    val category = getCategory(trans.category)
                    TransItem(
                        transaction = trans,
                        category = category,
                        currencyCode = currencyCode,
                        parName = "",

                        )

                }
            }
        }


    }
}

@Preview(showSystemUi = true)
@Composable
fun ParticipantContentPreview(){
    val parNameFieldValue = TextFieldValue("")
    val snackbarHostState = remember { SnackbarHostState() }
    val fakeParticipants = listOf(
        Participant(1, participantName = "Participant 1"),
        Participant(2, participantName = "Participant 2"),
        Participant(3, participantName = "Participant 3")
    )

    ParticipantContent(
        parNameFieldValue = parNameFieldValue,
        snackbarHostState = snackbarHostState,
        onChange = {},
        onSave = { /*TODO*/ },
        deletePar = { /*TODO*/ },
        allParticipant = fakeParticipants,
//        saveParticipant = {}
    )
}
