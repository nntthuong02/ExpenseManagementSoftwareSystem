package com.example.expensemanagement.presentation.transaction_screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.presentation.common.TabButton
import com.example.expensemanagement.presentation.navigation.Route
import com.example.expensemanagement.presentation.transaction_screen.component.Category
import com.example.expensemanagement.presentation.transaction_screen.component.FundDropdownMenu
import com.example.expensemanagement.presentation.transaction_screen.component.ParDropdownMenu
import com.example.expensemanagement.presentation.transaction_screen.component.ParticipantTag
import com.example.expensemanagement.presentation.transaction_screen.component.TabTransactionType
import kotlinx.coroutines.launch
import java.util.Calendar

//@Composable
//fun TransactionScreen() {
////    Text("TransactionScreen")
////    Column(modifier = Modifier.fillMaxSize()) {
////        TabTransactionType()
////        Spacer(modifier = Modifier.padding(10.dp))
////        FundDropdownMenu(onFundSelected = {})
////        ParticipantTag(participantName = "Thuong")
////    }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    navController: NavController,
//    transactionTag: Int?,
//    transactionDate: String?,
//    transactionPos: Int?,
//    transactionStatus: Int?,
    transactionViewModel: TransactionViewModel = hiltViewModel()
) {
    val selectedTransaction by transactionViewModel.tabButton.collectAsState()
//    val transactionType = TransactionType.values()[transactionTag!!]
    val scope = rememberCoroutineScope()
    val title by remember { mutableStateOf(transactionViewModel.transactionTitle) }
    val titleFieldValue = TextFieldValue(title.collectAsState().value)
    val transaction by remember { mutableStateOf(transactionViewModel.transactionAmount) }
    val transactionFieldValue = TextFieldValue(transaction.collectAsState().value)
    val currencyCode by transactionViewModel.selectedCurrencyCode.collectAsState()
    val selectedFundId by transactionViewModel.selectedFundId.collectAsState()
//    val number by remember { mutableStateOf(transactionViewModel.numberOfTeamT) }
//    val numberFieldValue = TextFieldValue(number.collectAsState().value)
//    val isPaid by remember { mutableStateOf(transactionViewModel.isPaidT) }
    var selectedFund by remember { mutableStateOf<Fund?>(null) }
    var selectedPar by remember { mutableStateOf<Participant?>(null) }
    val funds by transactionViewModel.fundByGroupId.collectAsState()
//    val funds by transactionViewModel.fundByGroupIdDto.collectAsState()

    //
//    val selectedParticipantName by transactionViewModel._participantName.collectAsState()
    val participantByFundId by transactionViewModel.participantByFundId.collectAsState()
    Log.d("TransactionScreen", "ok")
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            TabTransactionType()
            Spacer(modifier = Modifier.padding(5.dp))
            //Set Transaction title
            TextField(
                value = titleFieldValue.text,
                onValueChange = { field ->
                    transactionViewModel.setTransactionTitle(field)

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 5.dp,
                        top = 5.dp,
                        end = 5.dp,
                        bottom = 5.dp
                    ),
                maxLines = 1,
                singleLine = true,

                placeholder = {
                    Text(
                        text = if (selectedTransaction == TabButton.INCOME)
                            "Income title"
                        else "Expense title",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.LightGray
                )
            )
            //Set Transaction titile
            Log.d("Test TransactionType", "$selectedTransaction")
            Spacer(modifier = Modifier.padding(5.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 5.dp
                    )
            )
            Spacer(modifier = Modifier.padding(5.dp))
            FundDropdownMenu(
                onFundSelected = { fund ->
                    selectedFund = fund
                })
            //ParticipantTag
    //        LazyRow(
    //            horizontalArrangement = Arrangement.SpaceBetween,
    //            verticalAlignment = Alignment.CenterVertically,
    //            modifier = Modifier
    //                .padding(
    //                    horizontal = 5.dp,
    //                    vertical = 5.dp
    //                )
    //                .align(Alignment.Start)
    //        ) {
    //            participantByFundId.keys.forEach { key ->
    //                if (key == selectedFund?.fundId) {
    //                    val participants = participantByFundId[key]
    //                    if (participants != null) {
    //                        itemsIndexed(participants) { index, participant ->
    //                            ParDropdownMenu(
    //                                onParSelected = {
    //                                    selectedPar = it
    //                                }
    //                            )
    //                        }
    //                    }
    //                }
    //            }
    ////            participantByFundId.entries.forEach { key ->
    ////                println("Key: $key, Value: ${participantByFundId[key]}")
    ////            }
    //
    //        }
            Spacer(modifier = Modifier.padding(5.dp))
            Log.d("testParDropdownMenu1", "ok")

            participantByFundId.keys.forEach { key ->
                Log.d("testParDropdownMenu2", "ok")
                if (key == selectedFund?.fundId) {
                    val participants = participantByFundId[key]
                    Log.d("testParDropdownMenu2.2", participants.toString())
                    if (participants != null) {
                        participants.forEach { participant ->
                            Log.d("testParDropdownMenu2.2", "ok")
                            ParDropdownMenu(onParSelected = {
                                selectedPar = it
                            })
                        }
    //                        itemsIndexed(participants) { index, participant ->
    //                            ParDropdownMenu(
    //                                onParSelected = {
    //                                    selectedPar = it
    //                                }
    //                            )
    //                        }
                    }
                }
            }
            Log.d("testParDropdownMenu3", "ok")
            Text(
                text = if (selectedTransaction == TabButton.INCOME) {
                    "This is an Income: "
                } else "This is an Expense: ",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(
                        horizontal = 5.dp,
                        vertical = 5.dp
                    )
                    .align(Alignment.Start)
            )
            //Transaction amount
            TextField(
                value = transactionFieldValue.text,
                onValueChange = { field ->
                    Log.d("Test", "field ok")
                    transactionViewModel.setTransaction(field)
                    Log.d("test", "setTransaction Ok")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 5.dp,
                        top = 5.dp,
                        end = 5.dp,
                        bottom = 5.dp
                    ),
                maxLines = 1,
                singleLine = true,
                placeholder = {
                    Text(
                        text = "Enter the amount"
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.LightGray
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
            )
            //
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = "Set category"
            )

            Category()
    }
        SnackbarHost(hostState = snackbarHostState)
        Log.d("test selectedPar", selectedPar.toString())
        Button(onClick = { transactionViewModel.createEntity()}) {
        Text(text = "create")
    }
        Button(
            onClick = {
                if (titleFieldValue.text.isEmpty() || transactionFieldValue.text.isEmpty()) {
                    // Hiển thị Snackbar thông báo lỗi
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Please enter both title and amount.")
                    }
                } else {
                    transactionViewModel.apply {
                        setCurrentTime(Calendar.getInstance().time)
                        Log.d("Test transactionType", "$selectedTransaction")

                        if (selectedTransaction == TabButton.INCOME) {
                            addNewTransaction(
//                                0,
                                date.value,
                                transactionAmount.value.toDouble(),
                                category.value.title,
                                Constants.INCOME,
                                transactionTitle.value,
                                isPaid = false,
                                selectedPar?.participantId ?: 0
                            ) {
                                Log.d("test income transaction", "income success")
                                navController.navigateUp()
                                navController.navigate("${Route.TransactionScreen.route}")
                            }
                        } else {
                            Log.d("selectedFund", selectedFund?.totalAmount.toString())
                            if (selectedFund != null) {
                                funds.forEach { fund ->
                                    if (fund.fundId == selectedFund!!.fundId) {
                                        val newTotalAmount = fund.totalAmount + transactionAmount.value.toDouble()
                                        Log.d("UpdateFund", "Updating fund ${fund.fundId} with new total amount: $newTotalAmount")
                                        updateExpenseFund(
                                            fund.fundId,
                                            fund.fundName,
                                            newTotalAmount,
                                        )
                                    }
                                }
                            }

                            addNewTransaction(
//                                0,
                                date.value,
                                transactionAmount.value.toDouble(),
                                category.value.title,
                                Constants.EXPENSE,
                                transactionTitle.value,
                                isPaid = false,
                                selectedPar?.participantId ?: 0
                            ) {
                                Log.d("test expense transaction", "expense success")
                                navController.navigateUp()
                                navController.navigate("${Route.TransactionScreen.route}")
                            }
                        }
                    }
                    transactionViewModel.setTransactionTitle("")
                    transactionViewModel.setTransaction("")
//                navController.navigate("${Route.HomeScreen.route}")
                    //xu ly xoa het du lieu dang hien thi sau khi nhan "Save"
                }
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            Text(text = "Save")
        }

    }



}

fun showToast(context: Context?, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview(showSystemUi = true)
@Composable
fun TransactionScreenPreview(){
    TransactionScreen(
        navController = rememberNavController(),
    )
}

