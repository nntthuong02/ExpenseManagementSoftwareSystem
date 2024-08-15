package com.example.expensemanagement.presentation.insight_screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.common.Constants

import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.presentation.common.TabButton
import com.example.expensemanagement.presentation.insight_screen.component.ChooseDate

import com.example.expensemanagement.presentation.navigation.Route
import com.example.expensemanagement.presentation.transaction_screen.TransactionViewModel
import com.example.expensemanagement.presentation.transaction_screen.component.Category
import com.example.expensemanagement.presentation.transaction_screen.component.FundDropdownMenu
import com.example.expensemanagement.presentation.transaction_screen.component.ParDropdownMenu
import com.example.expensemanagement.presentation.transaction_screen.component.ParticipantTag
import com.example.expensemanagement.presentation.transaction_screen.component.TabTransactionType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import com.example.expensemanagement.presentation.common.Category
import com.example.expensemanagement.presentation.insight_screen.component.AlertDialogComponent
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.withContext
import java.util.Date
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    transactionId: Int,
    fundId: Int,
    navController: NavHostController,
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    insightViewModel: InsightViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val selectedTransaction by transactionViewModel.tabButton.collectAsState()
    val title by remember { mutableStateOf(transactionViewModel.transactionTitle) }
    val titleFieldValue = TextFieldValue(title.collectAsState().value)
    val transaction by remember { mutableStateOf(transactionViewModel.transactionAmount) }
    val transactionFieldValue = TextFieldValue(transaction.collectAsState().value)
    var selectedFund by remember { mutableStateOf<Fund?>(null) }
    var selectedPar by remember { mutableStateOf<Participant?>(null) }
    val funds by transactionViewModel.fundByGroupId.collectAsState()
    val participantByFundId by transactionViewModel.participantByFundId.collectAsState()
    val listFund by transactionViewModel.fundByGroupId.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val selectedDate by transactionViewModel.selectedDate.collectAsState()
    val openDialog = remember { mutableStateOf(false) }
    val transactionById by insightViewModel.transactionById.collectAsState()
    val fundById by insightViewModel.fundById.collectAsState()
    val parById by insightViewModel.parById.collectAsState()
    //Initial
    var initialTitle = remember { String() }
    var initialAmount = remember { String() }
    var initialType = remember { mutableStateOf<TabButton>(TabButton.INCOME) }
    val openAlertDialog = remember { mutableStateOf(false) }

    var initialDate = remember { Date() }


        val initialCategory by remember {
            mutableStateOf(transactionById?.category)
        }

        LaunchedEffect(transactionId, fundId) {
            insightViewModel.getTransById(transactionId)
            insightViewModel.getFundByFundId(fundId)
        }

        transactionById?.let { trans ->
            initialTitle = trans.title
            LaunchedEffect(initialTitle) {
                transactionViewModel.setTransactionTitle(initialTitle)
            }
            initialAmount = trans.amount.toString()
            LaunchedEffect(initialAmount) {
                transactionViewModel.setTransaction(initialAmount)
            }
//            transactionViewModel.setTransactionTitle(initialTitle)
//        if(trans.transactionType == Constants.INCOME){
//            initialType.value = TabButton.INCOME
//        } else{
//            initialType.value = TabButton.EXPENSE
//        }

//            transactionViewModel.setTransaction(initialAmount)
//        val category = Category.fromTitle(trans.category)
//        category?.let {
//            transactionViewModel.selectCategory(it)
//        }

            initialCategory?.let {
                val category = Category.fromTitle(it)
                category?.let {
                    transactionViewModel.selectCategory(it)
                }
            }

            selectedFund = fundById
            insightViewModel.getParById(trans.participantId)
            selectedPar = parById
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        openDialog.value = true
                    }) {
                        Text(text = transactionViewModel.convertDate(selectedDate))
                    }
//                Text(text = convertDate(selectedDate))
                }
                if (openDialog.value) {
                    ChooseDate(
                        onDismiss = {
                            openDialog.value = false
                        },
                        onConfirm = {
                            transactionViewModel.selectDate(it)
                        }
                    )
                }
                if (openAlertDialog.value == true) {
                    AlertDialogComponent(
                        onDismissRequest = { openAlertDialog.value = false },
                        onConfirmation = {
                            openAlertDialog.value = false
                            // Add logic here to handle confirmation.
                            coroutineScope.launch {
                                try {
//
                                    navController.navigate(Route.InsightScreen.route)
                                    Toast.makeText(context, "Erased!", Toast.LENGTH_SHORT).show()
                                    insightViewModel.eraseTransaction(transactionId)
//                                    {
//                                        popUpTo(Route.EditTransactionScreen.route) {
//                                            inclusive = true
//                                        }
//                                    }

//                                delay(300L) // Thời gian để màn hình chuyển đến HomeScreen

//                                insightViewModel.eraseTransaction(transactionId)
                                } catch (e: Exception) {
                                    // Handle exception if needed
                                    Log.e("YourComposable", "Error deleting transaction", e)
                                }
                            }
                        },
                        dialogTitle = "Confirmation delete",
                        dialogText = "Do you want to delete this transaction?",
                        icon = Icons.Default.Info
                    )
                }

                TabTransactionType(transactionId = transactionId)
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
                            text = initialTitle,
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
                //Set Transaction title
                Spacer(modifier = Modifier.padding(5.dp))
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 5.dp
                        )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Fund Name: ")
                FundDropdownMenu(
                    funds = listFund,
                    onFundSelected = { fund ->
                        selectedFund = fund
                    })
                Spacer(modifier = Modifier.padding(5.dp))
                Text(text = "Participant Name: ")
                participantByFundId.keys.forEach { key ->
                    if (key == selectedFund?.fundId) {
                        val participants = participantByFundId[key]
                        if (participants != null) {
//                            participants.forEach { participant ->
                                ParDropdownMenu(onParSelected = {
                                    selectedPar = it
                                },
                                    participants = participants
                                    )
//                            }
                        }
                    }
                }
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
                        transactionViewModel.setTransaction(field)
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
                            text = initialAmount
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
//        Button(onClick = { transactionViewModel.createEntity()}) {
//        Text(text = "create")
//        }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (titleFieldValue.text.isEmpty() || transactionFieldValue.text.isEmpty()) {
                            // Hiển thị Snackbar thông báo lỗi
                            coroutineScope.launch {

                                snackbarHostState.showSnackbar("This function will be updated soon")
                            }
                        } else {
                            transactionViewModel.apply {
                                setCurrentTime(Calendar.getInstance().time)

                                if (selectedTransaction == TabButton.INCOME) {
                                    coroutineScope.launch { snackbarHostState.showSnackbar("This function will be updated soon") }
                                } else {
                                    if (selectedFund != null) {
                                        funds.forEach { fund ->
                                            if (fund.fundId == selectedFund!!.fundId) {

                                                updateExpenseFund(
                                                    fund.fundId,
                                                    fund.fundName
                                                )
                                            }
                                        }
                                    }
                                    updateTransactionById(
                                        transactionId,
                                        selectedDate,
                                        transactionAmount.value.toDouble(),
                                        category.value.title,
                                        Constants.EXPENSE,
                                        transactionTitle.value,
                                        selectedPar?.participantId ?: 0,
                                        selectedFund?.fundId ?: 0,
                                        fundId
                                    ) {
//                                        navController.navigateUp()
//                                        navController.navigate("${Route.InsightScreen.route}")
                                    }
                                    transactionViewModel.setTransactionTitle("")
                                    transactionViewModel.setTransaction("")
                                    Toast.makeText(context, "Update successfully!", Toast.LENGTH_LONG)
                                        .show()
                                    navController.navigateUp()
                                }
                            }


//                        coroutineScope.launch {
//                            snackbarHostState.showSnackbar(
//                                message = "Update successfully",
//                                duration = SnackbarDuration.Short
//                            )
//                        }
//                navController.navigate("${Route.HomeScreen.route}")
                            //xu ly xoa het du lieu dang hien thi sau khi nhan "Save"
                        }
                    },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f)
                ) {

                    Text(text = "Update")
                }
                Button(
                    onClick = {
                        openAlertDialog.value = true
                    },
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f)
                ) {
                    Text(text = "Delete")
                }
            }
        }

}
@Preview(showSystemUi = true)
@Composable
fun EditTranPreview(){
    EditTransactionScreen(transactionId = 1, fundId = 1, navController = rememberNavController())
}