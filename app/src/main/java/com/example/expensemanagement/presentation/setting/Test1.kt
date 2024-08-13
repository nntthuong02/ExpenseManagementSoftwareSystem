//package com.example.expensemanagement.presentation.home
//
//import android.graphics.drawable.Icon
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.animation.AnimatedContent
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.expandVertically
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.shrinkVertically
//import androidx.compose.animation.slideInVertically
//import androidx.compose.animation.slideOutVertically
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.IntrinsicSize
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.Divider
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.Surface
//import androidx.compose.material3.TriStateCheckbox
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.state.ToggleableState
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.Density
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import com.example.expensemanagement.R
//import com.example.expensemanagement.common.Constants
//import com.example.expensemanagement.domain.models.Participant
//import com.example.expensemanagement.domain.models.Transaction
//import com.example.expensemanagement.presentation.common.TabContent
//import com.example.expensemanagement.presentation.home.component.CenterAlignedTopAppBar
//import com.example.expensemanagement.presentation.home.component.CheckBoxItem
//
//import com.example.expensemanagement.presentation.home.component.DetailEntityItem
//import com.example.expensemanagement.presentation.home.component.DialogName
//import com.example.expensemanagement.presentation.home.component.EditNameEntity
//import com.example.expensemanagement.presentation.home.component.TabBar
//import com.example.expensemanagement.presentation.home.component.TransItem
//import com.example.expensemanagement.presentation.insight_screen.component.AlertDialogComponent
//import com.example.expensemanagement.presentation.insight_screen.component.TransactionItem
//import com.example.expensemanagement.presentation.insight_screen.component.getCategory
//import com.example.expensemanagement.presentation.navigation.Route
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.async
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.launch
//
//@Composable
//fun Test1(
//    fundId: Int,
//    navController: NavHostController,
//    homeViewModel: HomeViewModel = hiltViewModel()
//) {
//    val selectedTab by homeViewModel.tabFund.collectAsState()
//    val fundById by homeViewModel.fundById.collectAsState()
//    val transByFund by homeViewModel.transByFund.collectAsState()
//    val allParticipant by homeViewModel.allParticipant.collectAsState()
//    val currencyCode by homeViewModel.selectedCurrencyCode.collectAsState()
//    val transWithPar by homeViewModel.transWithPar.collectAsState()
//    val participantsInFund by homeViewModel.parByFund.collectAsState()
//    val coroutineScope = rememberCoroutineScope()
//    val showSnack = remember { mutableStateOf(false) }
//    val snackbarHostState = remember { SnackbarHostState() }
//    val openAlertDialog = remember { mutableStateOf(false) }
//    val openDelelteDialog = remember { mutableStateOf(false) }
//    val showContent = remember { mutableStateOf(false) }
//    val childCheckedStates = homeViewModel.childCheckedStates
//    val showSnackbarText = remember{ mutableStateOf("") }
//    val openParDialog = remember { mutableStateOf(false) }
//
//
//    val context = LocalContext.current
//    var visible by remember { mutableStateOf(false) }
//    val density = LocalDensity.current
//    val fundTitle by remember { mutableStateOf(homeViewModel.fundName) }
//    val fundNameFieldValue = TextFieldValue(fundTitle.collectAsState().value)
//    LaunchedEffect(fundId) {
//        homeViewModel.apply {
//            getFundByFundId(fundId)
//            getTransactionByFund(fundId)
//            getAllPars()
//            getTransWithParByFund()
//            getParByFundId(fundId)
//
//        }
//    }
//
//    val initialPars = participantsInFund
//
//    coroutineScope.launch { homeViewModel.initializeStates(allParticipant, participantsInFund) }
//    if (transByFund != null) {
//        var sum = 0.0
//        transByFund.forEach { trans ->
//            if (trans.transactionType == Constants.EXPENSE) {
//                sum += trans.amount
//            }
//        }
//        homeViewModel.setExpense(sum)
//    }
//    if (fundById != null) {
//        showContent.value = true
//    }
//    var selectedParticipants =
//        allParticipant.filterIndexed { index, _ ->
//            childCheckedStates[index]
//        }
//    if(homeViewModel.containsInitialParticipants(initialPars, selectedParticipants)){
//        openParDialog.value = true
//    }
//
//    AnimatedContent(targetState = showContent.value, label = "VisibilityAnimation") { isVisible ->
//        if (isVisible) {
//            CenterAlignedTopAppBar(
//                showSnackbarText = showSnackbarText.value,
//                name = fundById!!.fundName,
//                rightIcon1 = R.drawable.edit_square_24px,
//                rightIcon2 = R.drawable.delete_24px,
//                iconOnclick1 = { openAlertDialog.value = true },
//                iconOnlick2 = { openDelelteDialog.value = true },
//                showIconRight1 = true,
//                showIconRight2 = true,
//                showIconLeft = true,
//                navController = navController,
//                showSnackbar = showSnack
//            ) { innerPadding ->
//                if (openAlertDialog.value == true) {
//                    DialogName(
//                        onDismissRequest = { openAlertDialog.value = false },
//                        onConfirmation = {
//
//                            if (fundNameFieldValue.text.isEmpty()) {
//                                openAlertDialog.value = false
//                                showSnackbarText.value = "Please enter name"
//                                showSnack.value = true
////                                snackbarHostState.showSnackbar("Please enter name")
//
//                            } else {
//                                openAlertDialog.value = false
//                                coroutineScope.launch {
//                                    homeViewModel.apply {
//                                        updateFundById(fundId, fundName.value, 1)
////                                    navController.navigateUp()
//                                        navController.navigate("${Route.EditFundScreen.route}/${fundById!!.fundId}")
//                                        Toast.makeText(
//                                            context,
//                                            "Fund name updated successfully",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    }
//                                }
//                            }
//
//                        },
//                        name = fundNameFieldValue.text,
//                        onNameChange = { text ->
//                            homeViewModel.setFundName(text)
//                        },
//                        dialogTitle = "Enter the name of the fund you want to create!",
//                        dialogText = "fund",
//                        iconId = R.drawable.post_add_24px
//                    )
//                }
//
//                if (openDelelteDialog.value) {
//                    AlertDialogComponent(
//                        onDismissRequest = { openDelelteDialog.value = false },
//                        onConfirmation = {
//                            if (fundId != 1) {
//                                openDelelteDialog.value = false
//                                navController.navigateUp()
//                                Toast.makeText(context, "erase successfully", Toast.LENGTH_SHORT)
//                                    .show()
//                                coroutineScope.launch {
//                                    homeViewModel.eraseFundByFundId(fundId)
//                                }
//                            } else {
//
//                                coroutineScope.launch {
//                                    snackbarHostState.showSnackbar("Cannot delete default fund!")
////                            Toast.makeText(context, "Cannot delete default fund!", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                        },
//                        dialogTitle = "Confirm deletion",
//                        dialogText = "Are you sure you want to delete this fund?",
//                        icon = Icons.Filled.Delete
//                    )
//                }
//
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues = innerPadding)
//                ) {
//                    // Row with buttons to switch between tabs
//                    TabBar(
//                        tab1 = TabContent.FUND,
//                        tab2 = TabContent.TRANSACTION,
//                        selectedTab = selectedTab
//                    ) { tabContent ->
//                        homeViewModel.setTabFund(tabContent)
//                    }
//
//                    AnimatedContent(targetState = selectedTab, label = "null") { targetTab ->
//                        when (targetTab) {
//                            TabContent.FUND -> FundContent(
//                                childCheckedStates = childCheckedStates,
////                                participantsInFund = participantsInFund,
//                                allParticipant = allParticipant,
//                                saveParticipant = {
//
//                                    coroutineScope.launch {
//
//                                        if (selectedParticipants.isEmpty()) {
//                                            showSnackbarText.value = "You must select at least one participant"
//                                            showSnack.value = true
////                                            snackbarHostState.showSnackbar("You must select at least one participant")
//                                        } else {
//                                            homeViewModel.addParticipantToFund(
//                                                allParticipant,
//                                                selectedParticipants,
//                                                fundId
//                                            )
//                                            Toast.makeText(
//                                                context,
//                                                "save participant successfully",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                    }
//
//                                },
//                                show = openParDialog.value,
//                                triStateOnClick = {index, check ->
//                                    homeViewModel.updateCheckedState(index, check)
//
//                                }
////                            deleteFund = {
////                                if (fundId != 1) {
////                                    coroutineScope.launch {
////                                        homeViewModel.eraseFundByFundId(fundId)
////                                    }
////
////                                    navController.navigateUp()
////                                    Toast.makeText(
////                                        context,
////                                        "erase successfully",
////                                        Toast.LENGTH_SHORT
////                                    ).show()
////                                } else {
////                                    coroutineScope.launch {
////                                        snackbarHostState.showSnackbar("Cannot delete default fund!")
//////                            Toast.makeText(context, "Cannot delete default fund!", Toast.LENGTH_SHORT).show()
////                                    }
////                                }
////                            }
//                            ){ index, check ->
//                                homeViewModel.updateCheckedState(index, check)
//                            }
//
//                            else -> {
//                                TransactionContent(
//                                    currencyCode = currencyCode,
//                                    transByFund = transByFund,
//                                    transWithPar = transWithPar,
//                                    onItemClick = {
//                                    }
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//    fundById?.let {
//
//    }
//}
//
//
//@Composable
//fun FundContent(
////    fundNameFieldValue: TextFieldValue,
////    snackbarHostState: SnackbarHostState,
////    onChange: (String) -> Unit,
////    onSave: () -> Unit,
////    deleteFund: () -> Unit,
//    show: Boolean,
//    allParticipant: List<Participant>,
//    saveParticipant: () -> Unit,
//    childCheckedStates: List<Boolean>,
//    triStateOnClick: (Int, Boolean) -> Unit,
//    onchangeCheck: (Int, Boolean) -> Unit,
//) {
//    val openParDialog = remember { mutableStateOf(false) }
////    val childCheckedStates = remember { mutableStateListOf<Boolean>() }
//    LaunchedEffect(allParticipant.size) {
////        childCheckedStates.addAll(List(allParticipant.size) { false })
////        allParticipant.forEachIndexed { index, participant ->
////            if (participantsInFund.contains(participant)) {
////                childCheckedStates[index] = true
////            } else {
////                childCheckedStates[index] = false
////            }
////        }
//    }
//    val parentState = when {
//        childCheckedStates.all { it } -> ToggleableState.On
//        childCheckedStates.none { it } -> ToggleableState.Off
//        else -> ToggleableState.Indeterminate
//    }
////    Column(
////        modifier = Modifier
////            .fillMaxWidth(),
////        verticalArrangement = Arrangement.SpaceBetween
////    ) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize(),
//        verticalArrangement = Arrangement.SpaceBetween
//    ) {
//        if (openParDialog.value){
//            AlertDialogComponent(
//                onDismissRequest = { openParDialog.value = false },
//                onConfirmation = {
//                    saveParticipant()
//                },
//                dialogTitle = "Confirm deletion",
//                dialogText = "Are you sure you want to delete this fund?",
//                icon = Icons.Filled.Delete
//            )
//        }
//
////        EditNameEntity(
////            nameEntity = "Fund",
////            name = fundNameFieldValue.text,
////            onNameChange = onChange
//////                {
//////                    homeViewModel.setFundName(it)
//////                }
////        ) {
////            onSave()
////        }
//
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
//                )
//            }
////                Text("Add participant")
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 5.dp, end = 5.dp)
//                    .height(IntrinsicSize.Min)
//                    .border(1.dp, Color.Black)
//            ) {
//                Box(
//                    modifier = Modifier
//                        .weight(0.7f)
//                        .padding(0.dp)
//                        .align(Alignment.CenterVertically)
//                ) {
//                    Text(
//                        text = "Select all",
//                        modifier = Modifier
//                            .padding(10.dp)
//                            .fillMaxWidth()
//                    )
//                }
//
//                Divider(
//                    color = Color.Black,
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .width(1.dp)
//                )
//
//                Box(
//                    modifier = Modifier
//                        .weight(0.3f)
//                        .padding(0.dp)
//                ) {
//                    TriStateCheckbox(
//                        modifier = Modifier
//                            .padding(0.dp)
//                            .align(Alignment.Center),
//                        state = parentState,
//                        onClick = {
//                            val newState = parentState != ToggleableState.On
//
//                            childCheckedStates.forEachIndexed { index, _ ->
//                                triStateOnClick(index, newState)
//                            }
//                        }
//                    )
//                }
//            }
//            Spacer(modifier = Modifier.padding(15.dp))
//            LazyColumn {
//                itemsIndexed(childCheckedStates) { index, checked ->
//                    CheckBoxItem(name = allParticipant[index].participantName, check = checked){isCheck ->
//                        onchangeCheck(index, isCheck)
//                    }
//                }
//            }
//
//        }
////            }
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
////            SnackbarHost(hostState = snackbarHostState)
//            Button(
//                modifier = Modifier.fillMaxWidth(),
//                onClick = {
//                    if (show != true){
//                        saveParticipant()
//                    }else {
//                        openParDialog.value = show
//                    }
//
//                }
//            ) {
//                Text(text = "Save participant")
//            }
//
//        }
//
//    }
////    }
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun TransactionContent(
//    currencyCode: String,
//    transByFund: List<Transaction>,
//    transWithPar: List<Pair<Transaction, Participant>>,
//    homeViewModel: HomeViewModel = hiltViewModel(),
//    onItemClick: (Int) -> Unit
//) {
//    val coroutineScope = rememberCoroutineScope()
////    val parId by homeViewModel.parById.collectAsState()
//    Surface(
//        color = MaterialTheme.colorScheme.background,
//        modifier = Modifier.padding(
//            top = 5.dp
//        )
//    ) {
//        if (transByFund.isEmpty()) {
//            Box(
//                modifier = Modifier
//                    .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
////                        .background(Color.LightGray) // Màu nền cho văn bản
//                    .padding(0.dp) // Khoảng cách giữa văn bản và viền nền
//                    .fillMaxWidth()
//            ) {
//                Text(
//                    text = "Not Transaction",
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
//
//        } else {
//            LazyColumn(
//                contentPadding = PaddingValues(
//                    start = 5.dp,
//                    top = 5.dp,
//                    end = 5.dp
//                )
//            ) {
////                item {
////                    Text(
////                        text = "Not Transaction",
////                        textAlign = TextAlign.Center,
////                        color = MaterialTheme.colorScheme.onBackground,
////                        style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Normal),
////                    )
////
////                }
//
//                itemsIndexed(transWithPar) { index, (trans, participant) ->
//
////                    coroutineScope.launch {
////                        homeViewModel.getParById(trans.participantId)
////                    }
//                    val category = getCategory(trans.category)
//
//                    TransItem(
//                        transaction = trans,
//                        category = category,
//                        currencyCode = currencyCode,
//                        parName = participant.participantName,
//
//                        )
//
//                }
//            }
//        }
//
//
//    }
//}
//
////@Preview(showSystemUi = true)
////@Composable
////fun EditFundScreenPreview() {
////    EditFundScreen(1, navController = rememberNavController())
////}
//
////@Preview(showBackground = true)
////@Composable
////fun FundContentPreview() {
////    val fakeParticipants = listOf(
////        Participant(1, participantName = "Participant 1"),
////        Participant(2, participantName = "Participant 2"),
////        Participant(3, participantName = "Participant 3")
////    )
////
////    FundContent(
//////        fundNameFieldValue = TextFieldValue("Sample Fund"),
//////        snackbarHostState = remember { SnackbarHostState() },
//////        onSave = {},
//////        onChange = {},
////        allParticipant = fakeParticipants,
////        participantsInFund = fakeParticipants,
////        saveParticipant = {},
////
//////        deleteFund = {}
////    )
////}
