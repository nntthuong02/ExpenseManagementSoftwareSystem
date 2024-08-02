package com.example.expensemanagement.presentation.home

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.domain.models.Transaction
import com.example.expensemanagement.presentation.common.TabContent

import com.example.expensemanagement.presentation.home.component.DetailEntityItem
import com.example.expensemanagement.presentation.home.component.EditNameEntity
import com.example.expensemanagement.presentation.home.component.TabBar
import com.example.expensemanagement.presentation.home.component.TransItem
import com.example.expensemanagement.presentation.insight_screen.component.TransactionItem
import com.example.expensemanagement.presentation.insight_screen.component.getCategory
import com.example.expensemanagement.presentation.navigation.Route
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Composable
fun EditFundScreen(
    fundId: Int,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val selectedTab by homeViewModel.tabFund.collectAsState()
    val fundById by homeViewModel.fundById.collectAsState()
    val transByFund by homeViewModel.transByFund.collectAsState()
    val expense by homeViewModel.expense.collectAsState()
    val allParticipant by homeViewModel.allParticipant.collectAsState()
    val participantByFund by homeViewModel.parByFund.collectAsState()
    val currencyCode by homeViewModel.selectedCurrencyCode.collectAsState()
    val parById by homeViewModel.parById.collectAsState()
    val transWithPar by homeViewModel.transWithPar.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var visible by remember { mutableStateOf(false) }
    val density = LocalDensity.current
    val fundTitle by remember { mutableStateOf(homeViewModel.fundName) }
    val fundNameFieldValue = TextFieldValue(fundTitle.collectAsState().value)
    Log.d("fundId editfundscreen", fundId.toString())
    LaunchedEffect(fundId) {
        homeViewModel.apply {
            getFundByFundId(fundId)
            Log.d("fundId", fundId.toString())
            Log.d("transByFund1", transByFund.toString())
            getTransactionByFund(fundId)
            getAllPars()
            getTransWithPar()
        }
        Log.d("edit fund screen", fundId.toString())
    }
//    LaunchedEffect(transByFund){
        Log.d("LaunchedEffect(transByFund)", transByFund.toString())
        if (transByFund != null) {
            var sum = 0.0
            transByFund.forEach { trans ->
                if (trans.transactionType == Constants.EXPENSE) {
                    sum += trans.amount
                }
            }
            homeViewModel.setExpense(sum)
        }
//    }

//    val childCheckedStates = remember { mutableStateListOf<Boolean>() }
//    childCheckedStates.addAll(List(allParticipant.size) { false })
//    val parentState = when {
//        childCheckedStates.all { it } -> ToggleableState.On
//        childCheckedStates.none { it } -> ToggleableState.Off
//        else -> ToggleableState.Indeterminate
//    }
//    val fundContent: @Composable () -> Unit = {}
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Row with buttons to switch between tabs
        TabBar(entityTab = TabContent.FUND, selectedTab = selectedTab) { tabContent ->
            homeViewModel.setTabFund(tabContent)
        }

        // Content below changes based on the content variable
//        content()
        AnimatedContent(targetState = selectedTab) { targetTab ->
            when (targetTab) {
                TabContent.FUND -> FundContent(
                    fundNameFieldValue = fundNameFieldValue,
                    snackbarHostState = snackbarHostState,
                    onChange = {
                        homeViewModel.setFundName(it)
                        Log.d("onChange", it)
                        Log.d("onChange fund name", homeViewModel.fundName.value)
                    },
                    onSave = {
                        Log.d("fundNameFieldValue", fundNameFieldValue.text)
                        if (fundNameFieldValue.text.isEmpty()) {
                            // Hiển thị Snackbar thông báo lỗi
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please enter name")
                            }
                        } else {
                            coroutineScope.launch {
                                homeViewModel.apply {
                                    updateFundById(fundId, fundName.value, 1)
                                    Log.d("fundNameFieldValue", fundNameFieldValue.text)
                                    navController.navigateUp()
                                    navController.navigate("${Route.EditFundScreen.route}/${fundById!!.fundId}")
                                }
                            }
                        }
                    },
                    allParticipant = allParticipant,
                    saveParticipant = { listChecked ->
                        val selectedParticipants = allParticipant.filterIndexed { index, _ ->
                            listChecked[index]
                        }
                        coroutineScope.launch{
                            homeViewModel.addParticipantToFund(selectedParticipants, fundId)
                            Toast.makeText(context, "save participant successfully", Toast.LENGTH_SHORT).show()
                        }

                    },
                    deleteFund = {
                        if (fundId != 1){
                            coroutineScope.launch {
                                homeViewModel.eraseFundByFundId(fundId)
                            }
//                        navController.navigate("${Route.ListFundScreen.route}")

                        navController.navigateUp()
                        Toast.makeText(context, "erase successfully", Toast.LENGTH_SHORT).show()
                    } else{
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Cannot delete default fund!")
//                            Toast.makeText(context, "Cannot delete default fund!", Toast.LENGTH_SHORT).show()
                        }
                        }
                    }
                )

                else -> {
                    TransactionContent(
                        currencyCode = currencyCode,
                        transByFund = transByFund,
                        transWithPar = transWithPar,
                        onItemClick = {
                        }
                    )
                }
            }
        }
    }

}


@Composable
fun FundContent(
    fundNameFieldValue: TextFieldValue,
    snackbarHostState: SnackbarHostState,
    onChange: (String) -> Unit,
    onSave: () -> Unit,
    deleteFund: () -> Unit,
    allParticipant: List<Participant>,
    saveParticipant: (List<Boolean>)  -> Unit,
) {
    val childCheckedStates = remember { mutableStateListOf<Boolean>() }
    LaunchedEffect(allParticipant.size){
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
                nameEntity = "Fund",
                name = fundNameFieldValue.text,
                onNameChange = onChange
//                {
//                    homeViewModel.setFundName(it)
//                }
            ) {
                onSave()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Parent TriStateCheckbox
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
//                        .background(Color.LightGray) // Màu nền cho văn bản
                        .padding(0.dp) // Khoảng cách giữa văn bản và viền nền
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Add paritcipant",
                        modifier = Modifier

                            .align(Alignment.Center)
                            .padding(8.dp), // Tạo khoảng cách giữa văn bản và đường viền
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,

                        )
                }
//                Text("Add participant")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Select all        ")
                    TriStateCheckbox(
                        state = parentState,
                        onClick = {
                            val newState = parentState != ToggleableState.On
                            childCheckedStates.forEachIndexed { index, _ ->
                                childCheckedStates[index] = newState
                            }
                        }
                    )
                }
                LazyColumn {
                    itemsIndexed(childCheckedStates) { index, checked ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text("${allParticipant[index].participantName}     ")
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { isChecked ->
                                    childCheckedStates[index] = isChecked
                                }
                            )
                        }
                    }
                }

            }
//            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                SnackbarHost(hostState = snackbarHostState)
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { saveParticipant(childCheckedStates) }
                ) {
                    Text(text = "Save participant")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = deleteFund
                ) {
                    Text(text = "Delete fund")
                }
            }

        }
//    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionContent(
    currencyCode: String,
    transByFund: List<Transaction>,
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
        Log.d("transByFund Trans", transByFund.toString())
        if(transByFund == null) {
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

        } else{
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
                Log.d("test ParticipantDetail", transWithPar.toString())

                itemsIndexed(transWithPar) { index, (trans, participant) ->

//                    coroutineScope.launch {
//                        homeViewModel.getParById(trans.participantId)
//                    }
                    val category = getCategory(trans.category)
                    Log.d("test Transaction", trans.toString()

                    )
                    TransItem(
                        transaction = trans,
                        category = category,
                        currencyCode = currencyCode,
                        parName = participant.participantName,

                        )

                }
            }
        }


    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun EditFundScreenPreview() {
//    EditFundScreen(1, navController = rememberNavController())
//}

@Preview(showBackground = true)
@Composable
fun FundContentPreview() {
    val fakeParticipants = listOf(
        Participant(1, participantName = "Participant 1"),
        Participant(2, participantName = "Participant 2"),
        Participant(3, participantName = "Participant 3")
    )

    FundContent(
        fundNameFieldValue = TextFieldValue("Sample Fund"),
        snackbarHostState = remember { SnackbarHostState() },
        onSave = {},
        onChange = {},
        allParticipant = fakeParticipants,
        saveParticipant = {},
        deleteFund = {}
        )
}
