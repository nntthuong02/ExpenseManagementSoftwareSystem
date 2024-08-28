//package com.example.expensemanagement.presentation.transaction_screen
//import android.content.Context
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Button
//import androidx.compose.material3.Divider
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.SnackbarDuration
//import androidx.compose.material3.SnackbarHost
//import androidx.compose.material3.SnackbarHostState
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.example.expensemanagement.common.Constants
//import com.example.expensemanagement.domain.models.Fund
//import com.example.expensemanagement.domain.models.Participant
//import com.example.expensemanagement.presentation.common.TabButton
//import com.example.expensemanagement.presentation.insight_screen.component.ChooseDate
//
//import com.example.expensemanagement.presentation.navigation.Route
//import com.example.expensemanagement.presentation.transaction_screen.component.Category
//import com.example.expensemanagement.presentation.transaction_screen.component.FundDropdownMenu
//import com.example.expensemanagement.presentation.transaction_screen.component.ParDropdownMenu
//import com.example.expensemanagement.presentation.transaction_screen.component.ParticipantTag
//import com.example.expensemanagement.presentation.transaction_screen.component.TabTransactionType
//import com.example.expensemanagement.presentation.transaction_screen.component.TransactionForm
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import java.util.Calendar
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ScreenTest(
//    navController: NavController,
//    transactionViewModel: TransactionViewModel = hiltViewModel()
//) {
//    val selectedTransaction by transactionViewModel.tabButton.collectAsState()
//    val title by remember { mutableStateOf(transactionViewModel.transactionTitle) }
//    val titleFieldValue = TextFieldValue(title.collectAsState().value)
//    val transaction by remember { mutableStateOf(transactionViewModel.transactionAmount) }
//    val transactionFieldValue = TextFieldValue(transaction.collectAsState().value)
//    var selectedFund by remember { mutableStateOf<Fund?>(null) }
//    var selectedPar by remember { mutableStateOf<Participant?>(null) }
//    val funds by transactionViewModel.fundByGroupId.collectAsState()
//    val participantByFundId by transactionViewModel.participantByFundId.collectAsState()
//    val snackbarHostState = remember { SnackbarHostState() }
//    val coroutineScope = rememberCoroutineScope()
//    val selectedDate by transactionViewModel.selectedDate.collectAsState()
//
//    TransactionForm(
//        selectedTransaction = selectedTransaction,
//        titleFieldValue = titleFieldValue,
//        onTitleChange = { title ->
//            transactionViewModel.setTransactionTitle(title)
//        },
//        transactionFieldValue = transactionFieldValue,
//        onTransactionChange = { transaction ->
//            transactionViewModel.setTransaction(transaction)
//        },
//        funds = funds,
//        participantByFundId = participantByFundId,
//        selectedFund = selectedFund,
//        onFundSelected = { fund ->
//            selectedFund = fund
//        },
//        selectedPar = selectedPar,
//        onParSelected = { participant ->
//            selectedPar = participant
//        },
//        selectedDate = transactionViewModel.convertDate(selectedDate),
//        onDateChange = { date ->
////            transactionViewModel.selectDate(d)
//        },
//        category = "Category placeholder", // Update with your actual category logic
//        onSave = {
//            if (titleFieldValue.text.isEmpty() || transactionFieldValue.text.isEmpty()) {
//                coroutineScope.launch {
//                    snackbarHostState.showSnackbar("Please enter both title and amount")
//                }
//            } else {
//                transactionViewModel.apply {
//                    setCurrentTime(Calendar.getInstance().time)
//                    if (selectedTransaction == TabButton.INCOME) {
//                        addNewTransaction(
//                            selectedDate,
//                            "",
//                            transactionAmount.value.toDouble(),
//                            category.value.title,
//                            Constants.INCOME,
//                            transactionTitle.value,
//                            isPaid = false,
//                            selectedPar?.participantId ?: 0
//                        ) {
//                            navController.navigateUp()
//                            navController.navigate("${Route.TransactionScreen.route}")
//                        }
//                    } else {
//                        if (selectedFund != null) {
//                            funds.forEach { fund ->
//                                if (fund.fundId == selectedFund!!.fundId) {
//                                    val newTotalAmount = fund.totalAmount + transactionAmount.value.toDouble()
//                                    updateExpenseFund(
//                                        fund.fundId,
//                                        fund.fundName,
//                                        newTotalAmount
//                                    )
//                                }
//                            }
//                        }
//                        addNewTransaction(
//                            selectedDate,
//                            "",
//                            transactionAmount.value.toDouble(),
//                            category.value.title,
//                            Constants.EXPENSE,
//                            transactionTitle.value,
//                            isPaid = false,
//                            selectedPar?.participantId ?: 0
//                        ) {
//                            navController.navigateUp()
//                            navController.navigate("${Route.TransactionScreen.route}")
//                        }
//                    }
//                }
//                transactionViewModel.setTransactionTitle("")
//                transactionViewModel.setTransaction("")
//                coroutineScope.launch {
//                    snackbarHostState.showSnackbar(
//                        message = "Saved successfully",
//                        duration = SnackbarDuration.Long
//                    )
//                }
//            }
//        }
//    )
//}
