package com.example.expensemanagement.presentation.home

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.R
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.presentation.common.TabContent
import com.example.expensemanagement.presentation.home.component.CardWithIcon
import com.example.expensemanagement.presentation.home.component.EditNameEntity
import com.example.expensemanagement.presentation.home.component.EntityItem
import com.example.expensemanagement.presentation.home.component.FundBarChart
import com.example.expensemanagement.presentation.home.component.ParBarChart
import com.example.expensemanagement.presentation.home.component.TabBar
import com.example.expensemanagement.presentation.home.component.TabBarTransparent
import com.example.expensemanagement.presentation.insight_screen.component.AlertDialogComponent
import com.example.expensemanagement.presentation.navigation.Route
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val groupById by homeViewModel.groupById.collectAsState()
    val parById by homeViewModel.parById.collectAsState()
    val fundsByGroup by homeViewModel.fundByGroupId.collectAsState()
    val allPar by homeViewModel.allParticipant.collectAsState()
    val openAlertDialog = remember { mutableStateOf(false) }
    val allTrans by homeViewModel.allTransaction.collectAsState()
    val unpaidTrans by homeViewModel.unpaidTrans.collectAsState()
    val fundExpenseUnPaid by homeViewModel.fundExpenseUnPaid.collectAsState()
    val parExpenseUnPaid by homeViewModel.parExpenseUnPaid.collectAsState()
    val selectedTab by homeViewModel.tabFund.collectAsState()

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    Log.d("_parExpenseUnPaid", fundExpenseUnPaid.toString())
    Log.d("selectedTab", selectedTab.title)

//    var initialGroupName = remember { String() }
    LaunchedEffect(fundExpenseUnPaid) {
        launch { homeViewModel.getFundByGroup() }
        launch { homeViewModel.getAllPars() }
        launch { homeViewModel.getParById(1) }
        launch { homeViewModel.fetchAllTransactions() }
        launch { homeViewModel.fetchFundExpenseUnPaid() }
        launch { homeViewModel.fetchParExpenseUnPaid() }
//            launch { homeViewModel.fetchUnpaidTransactions() }


    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 5.dp),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = buildAnnotatedString {
                withStyle(
                    SpanStyle(

                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        letterSpacing = 0.4.sp,
                        color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.background)
                    )
                ) {
                    append("Welcome,  ")
                }
                withStyle(
                    SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 0.6.sp,
                        color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.background)
                    )
                ) {
                    append(parById?.participantName + "!")
                }
            })
        }

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.background),
            text = "Overview"
        )

        Spacer(modifier = Modifier.padding(5.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp)
        ) {
            item {
                CardWithIcon(
                    nameEntity = "Funds",
                    number = fundsByGroup.size.toString(),
                    idIcon = R.drawable.currency_exchange_24px
                ) {
                    navController.navigate(Route.ListFundScreen.route)
                }
                Spacer(modifier = Modifier.padding(5.dp))
            }
            item {
                CardWithIcon(
                    nameEntity = "Participants",
                    number = allPar.size.toString(),
                    R.drawable.person_24px
                ) {
                    navController.navigate(Route.ListParticipantScreen.route)
                }
                Spacer(modifier = Modifier.padding(5.dp))
            }
            item {
                CardWithIcon(
                    nameEntity = "Transactions",
                    number = allTrans.size.toString(),
                    R.drawable.list_alt_24px
                ) {
                    navController.navigate(Route.ListTransaction.route)
                }
            }
        }

        Spacer(modifier = Modifier.padding(5.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
            TabBarTransparent(
                tab1 = TabContent.FUND,
                tab2 = TabContent.PARTICIPANT,
                selectedTab = selectedTab
            ) { tabContent ->
                homeViewModel.setTabFund(tabContent)
            }
        }
//        Row(
//            Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Card(onClick = { navController.navigate(Route.StatisticsScreen.route)}) {
//                Text(text = "StatisticsScreen")
//            }
////            TabBar(tab1 = , tab2 = , selectedTab = , onTabSelected = )
//        }
        val fundExpense = fundExpenseUnPaid.map { (fund, expense) ->
            fund to homeViewModel.formatDouble(expense / 1000.0)
        }
        val parExpense = parExpenseUnPaid.map { (par, expense) ->
            par to homeViewModel.formatDouble(expense / 1000.0)
        }

        if (fundExpenseUnPaid.isNotEmpty() && parExpenseUnPaid.isNotEmpty()) {
            AnimatedContent(targetState = selectedTab) { targetTab ->

                when (targetTab) {
                    TabContent.FUND -> FundBarChart(
                        oxLabel = "Fund",
                        oyLabel = "Money(thousand)",
                        chartData = fundExpense
                    )

                    else -> ParBarChart(
                        oxLabel = "Participant",
                        oyLabel = "Money(thousand)",
                        chartData = parExpense
                    )
                }
            }
        }

        // List fund
//        EntityItem(
//            nameEntity = "Fund",
//            number = fundsByGroup.size.toString(),
//            itemOnClick = {
//                          navController.navigate(Route.ListFundScreen.route)
//                          },
//            backgroundColor = Color.DarkGray.copy(alpha= 0.1f),
//            surfaceColor = Color.Blue
//        )
//        Spacer(modifier = Modifier.padding(5.dp))
//        //List participant
//        EntityItem(
//            nameEntity = "Participant",
//            number = allPar.size.toString(),
//            itemOnClick = {
//                          navController.navigate(Route.ListParticipantScreen.route)
//                          },
//            backgroundColor = Color.DarkGray.copy(alpha= 0.1f),
//            surfaceColor = Color.Green
//        )
//        Spacer(modifier = Modifier.padding(5.dp))
//        if (openAlertDialog.value == true) {
//            AlertDialogComponent(
//                onDismissRequest = { openAlertDialog.value = false },
//                onConfirmation = {
//                    openAlertDialog.value = false
//                    // Add logic here to handle confirmation.
//                    coroutineScope.launch {
//                        try {
////                                navController.navigateUp()
//
//                            homeViewModel.eraseAllTransactions()
//
//                            navController.navigate(Route.HomeScreen.route)
//                            Toast.makeText(context, "Erased successfully!", Toast.LENGTH_SHORT).show()
//                        } catch (e: Exception) {
//                            // Handle exception if needed
//                            Log.e("YourComposable", "Error deleting transaction", e)
//                        }
//                    }
//                },
//                dialogTitle = "Confirmation delete",
//                dialogText = "Do you want to delete all transactions?",
//                icon = Icons.Default.Info
//            )
//        }
//        Spacer(modifier = Modifier.padding(5.dp))
//        Row(modifier = Modifier.fillMaxWidth()) {
//            Button(
//                onClick = { openAlertDialog.value = true },
//                shape = MaterialTheme.shapes.small,
//                modifier = Modifier
//                    .padding(5.dp)
//                    .weight(1f)
//            ) {
//                Text(text = "Delete All Transactions")
//            }
//
//        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}