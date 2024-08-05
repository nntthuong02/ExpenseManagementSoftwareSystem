package com.example.expensemanagement.presentation.payment_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.presentation.common.TabContent
import com.example.expensemanagement.presentation.home.component.TabBar
import com.example.expensemanagement.presentation.insight_screen.component.AlertDialogComponent
import com.example.expensemanagement.presentation.navigation.Route
import com.example.expensemanagement.presentation.payment_screen.component.RowItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

@Composable
fun PaymentScreen(
    navController: NavHostController,
    paymentViewModel: PaymentViewModel = hiltViewModel()
) {
    val fundAndExpense by paymentViewModel.fundAndExpense.collectAsState()
    val parAndAmount by paymentViewModel.parAndExpense.collectAsState()
    val transByDate by paymentViewModel.transactionsByDate.collectAsState()

    val selectedTab by paymentViewModel.tabBar.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Log.d("fundAndExpense", fundAndExpense.toString())
    Log.d("parAndAmount", parAndAmount.toString())

    LaunchedEffect(Unit){
        paymentViewModel.getPaidTransactionCountsByDate()
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabBar(tab1 = TabContent.PAYMENT, tab2 = TabContent.HISTORY, selectedTab = selectedTab) {
            paymentViewModel.setTabBar(it)
        }
        AnimatedContent(targetState = selectedTab) { targetTab ->
            when (targetTab) {
                TabContent.PAYMENT -> PaymentContent(
                    fundAndExpense = fundAndExpense,
                    parAndExpense = parAndAmount,
                    confirmClick = {
                        // Add logic here to handle confirmation.
                        coroutineScope.launch {
                            try {
//                                navController.navigateUp()
                                val time = paymentViewModel.convertDate(Calendar.getInstance().time)

                                paymentViewModel.paymentExpense(time)
//                                paymentViewModel.eraseTransaction(transactionId)
//                                delay(300L)
                                navController.navigate(Route.PayScreen.route)
//

                                Toast.makeText(context, "Payment success!", Toast.LENGTH_SHORT).show()
//                                insightViewModel.eraseTransaction(transactionId)
                            } catch (e: Exception) {
                                // Handle exception if needed
                                Log.e("YourComposable", "Error Payment ", e)
                            }
                        }
                    }
                )
                else -> HistoryPayment(
                    transAndNumber = transByDate,
                    confirmClick = {
                        coroutineScope.launch {
                            try {
                                paymentViewModel.undoPay()
                                navController.navigate(Route.PayScreen.route)
                                Toast.makeText(context, "Undo payment success!", Toast.LENGTH_SHORT).show()
                            }catch (e: Exception){
                                Log.e("YourComposable", "Error Undo ", e)
                            }
                        }
                    }
                )
            }

        }
    }
}

@Composable
fun PaymentContent(
    fundAndExpense: List<Pair<Fund, Double>>,
    parAndExpense: List<Pair<Participant, Double>>,
    confirmClick: () -> Unit,
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val item1 = (1..5).toList()
    val item2 = (1..5).toList()
    val listFund = listOf(
        Fund(1, "Quy 2", 1),
        Fund(1, "Quy 2", 1),
        Fund(1, "Quy 2", 1),
        Fund(1, "Quy 2", 1),
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(0.9f)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // Chiếm phần không gian còn lại
                ){
                    item {
                        Box(
                            modifier = Modifier
                                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
//                        .background(Color.LightGray) // Màu nền cho văn bản
                                .padding(0.dp) // Khoảng cách giữa văn bản và viền nền
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Fund Fee Schedule",
                                modifier = Modifier

                                    .align(Alignment.Center)
                                    .padding(8.dp), // Tạo khoảng cách giữa văn bản và đường viền
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
//                    Spacer(modifier = Modifier.padding(15.dp))
                        RowItem(name = "Fund", expense = "Expense")
                    }
                    itemsIndexed(fundAndExpense){index, (fund, expense) ->
                        RowItem(name = fund.fundName, expense = expense.toString())
                    }
                    item {
                        Spacer(modifier = Modifier.padding(5.dp))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxWidth()
            ) {




                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // Chiếm phần không gian còn lại
                ){

                    item {
                        Box(
                            modifier = Modifier
                                .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
//                        .background(Color.LightGray) // Màu nền cho văn bản
                                .padding(0.dp) // Khoảng cách giữa văn bản và viền nền
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Payment Schedule",
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(8.dp), // Tạo khoảng cách giữa văn bản và đường viền
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
//                    Spacer(modifier = Modifier.padding(15.dp))
                        RowItem(name = "Participant", expense = "Payment Amount")
                    }
                    itemsIndexed(parAndExpense){index, (par, amount) ->
                        RowItem(name = par.participantName, expense = amount.toString())
                    }
                }
            }
        }
        if (openAlertDialog.value == true) {
            AlertDialogComponent(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                    confirmClick()
                                 },
                dialogTitle = "Payment Confirmation",
                dialogText = "Are you sure you want to pay?",
                icon = Icons.Default.Info
            )
        }

        Row(
            modifier = Modifier
                .weight(0.1f)
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { openAlertDialog.value = true }
            ) {
                Text(text = "Pay")
            }
        }
    }
}

//    LazyVerticalGrid(
//        columns = GridCells.Fixed(2), // Số cột cố định
//        modifier = Modifier.padding(16.dp)
//    ) {
//        items(item1.size) { item ->
//            Box(modifier = Modifier
//                .border(1.dp, Color.Black)
//            ){
//                Text(
//                    text = "Item ${item1[item]}",
//                    modifier = Modifier.padding(8.dp)
//                )
//            }
//
//        }
//    }
@Composable
fun HistoryPayment(

    transAndNumber: List<Pair<String, Int>>,
    confirmClick: () -> Unit,
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(0.9f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Chiếm phần không gian còn lại
            ){
                item {
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
//                        .background(Color.LightGray) // Màu nền cho văn bản
                            .padding(0.dp) // Khoảng cách giữa văn bản và viền nền
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Payment date table",
                            modifier = Modifier

                                .align(Alignment.Center)
                                .padding(8.dp), // Tạo khoảng cách giữa văn bản và đường viền
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
//                    Spacer(modifier = Modifier.padding(15.dp))
                    RowItem(name = "Date of payment", expense = "Number of transactions ")
                }
                itemsIndexed(transAndNumber){index, (date, number) ->
                    RowItem(name = date, expense = number.toString())
                }
                item {
                    Spacer(modifier = Modifier.padding(5.dp))
                }
            }
        }
        if (openAlertDialog.value == true) {
            AlertDialogComponent(
                onDismissRequest = { openAlertDialog.value = false },
                onConfirmation = {
                    openAlertDialog.value = false
                    confirmClick()
                },
                dialogTitle = "Payment Confirmation",
                dialogText = "Are you sure you want to pay?",
                icon = Icons.Default.Info
            )
        }

        Row(
            modifier = Modifier
                .weight(0.1f)
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp)
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { openAlertDialog.value = true }
            ) {
                Text(text = "Undo payment")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPaymentContent() {
    val fund = Fund(1, "Quy 2", 1)
    val par = Participant(1, "Thuong")
    val fundAndExpense = listOf(
        Pair(fund, 1.0)
    )
    val parAndAmount = listOf(
        Pair(par, 1.0)
    )
    PaymentContent(fundAndExpense, parAndAmount, confirmClick = {})
}