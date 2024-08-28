package com.example.expensemanagement.presentation.payment_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.presentation.common.TabContent
import com.example.expensemanagement.presentation.home.component.TabBar
import com.example.expensemanagement.presentation.insight_screen.component.AlertDialogComponent
import com.example.expensemanagement.presentation.navigation.Route
import com.example.expensemanagement.presentation.payment_screen.component.RowItem
import com.example.expensemanagement.ui.theme.Blue1
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
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

    LaunchedEffect(Unit) {
        paymentViewModel.getPaidTransactionCountsByDate()
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabBar(tab1 = TabContent.PAYMENT, tab2 = TabContent.HISTORY, selectedTab = selectedTab) {
            paymentViewModel.setTabBar(it)
        }
        AnimatedContent(targetState = selectedTab, label = "Payment") { targetTab ->
            when (targetTab) {
                TabContent.PAYMENT -> PaymentContent(
                    fundAndExpense = fundAndExpense,
                    parAndExpense = parAndAmount,
                    confirmClick = {
                        // Add logic here to handle confirmation.
                        coroutineScope.launch {
                            try {
                                val time = paymentViewModel.convertDate(Calendar.getInstance().time)
                                paymentViewModel.paymentExpense(time)
                                navController.navigate(Route.PayScreen.route)
                                Toast.makeText(context, "Payment success!", Toast.LENGTH_SHORT)
                                    .show()
                            } catch (e: Exception) {
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
                                Toast.makeText(context, "Undo payment success!", Toast.LENGTH_SHORT)
                                    .show()
                            } catch (e: Exception) {
                                Log.e("YourComposable", "Error Undo ", e)
                            }
                        }
                    }
                )
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PaymentContent(
    fundAndExpense: List<Pair<Fund, Double>>,
    parAndExpense: List<Pair<Participant, Double>>,
    confirmClick: () -> Unit,
) {
    val openAlertDialog = remember { mutableStateOf(false) }
//    val item1 = (1..5).toList()
//    val item2 = (1..5).toList()
//    val listFund = listOf(
//        Fund(1, "Quy 2", 1),
//        Fund(1, "Quy 2", 1),
//        Fund(1, "Quy 2", 1),
//        Fund(1, "Quy 2", 1),
//    )
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
                ) {
                    stickyHeader {
                        Surface(
                            modifier = Modifier
                                .wrapContentHeight()
                                .zIndex(1f)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Column {
                                Box(
                                    modifier = Modifier
                                        .background(Blue1)
                                        .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                                        .padding(0.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Fund Fee Schedule",
                                        modifier = Modifier

                                            .align(Alignment.Center)
                                            .padding(8.dp),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
//                    Spacer(modifier = Modifier.padding(15.dp))
                                RowItem(name = "Fund", expense = "Expense")
                            }

                        }
                    }
                    item {

                    }
                    itemsIndexed(fundAndExpense) { index, (fund, expense) ->
                        val amount = formatAmount(expense)
                        RowItem(name = fund.fundName, expense = amount)
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
                ) {

                    stickyHeader {
                        Surface(
                            modifier = Modifier
                                .height(IntrinsicSize.Min)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Column { // Đặt Box và RowItem vào Column
                                Box(
                                    modifier = Modifier
                                        .background(Color.Blue.copy(0.5f))
                                        .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                                        .fillMaxWidth()
                                        .padding(8.dp) // Đảm bảo khoảng cách hợp lý
                                ) {
                                    Text(
                                        text = "Participant Fee Schedule",
                                        modifier = Modifier.align(Alignment.Center),
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
                                RowItem(name = "Participant", expense = "Payment Amount")
                            }
                        }
                    }
                    item {

                    }
                    itemsIndexed(parAndExpense) { index, (par, amount) ->
                        val amountFormat = formatAmount(amount)
                        RowItem(name = par.participantName, expense = amountFormat)
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

@Composable
fun HistoryPayment(

    transAndNumber: List<Pair<String, Int>>,
    confirmClick: () -> Unit,
) {
    val openAlertDialog = remember { mutableStateOf(false) }
    val dialogTitle = remember { mutableStateOf("") }
    val dialogText = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(0.8f)
                .fillMaxWidth()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Chiếm phần không gian còn lại
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .background(Color.Green)
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
                    RowItem(name = "Trans number ", expense = "Date of payment")
                }
                itemsIndexed(transAndNumber) { index, (date, number) ->
                    RowItem(name = number.toString(), expense = date)
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
                dialogTitle = dialogTitle.value,
                dialogText = dialogText.value,
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
                onClick = {
                    openAlertDialog.value = true
                    dialogTitle.value = "Confirm undo payment"
                    dialogText.value = "Are you sure you want to undo the payment?"
                }
            ) {
                Text(text = "Undo payment")
            }
        }
    }
}
fun formatAmount(value: Double): String {
    if (value == 0.0) {
        return "0,0"
    }
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        decimalSeparator = ','
        groupingSeparator = '.'
    }
    val format = DecimalFormat("#,###.0", symbols)

    return format.format(value)
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