//package com.example.expensemanagement.presentation.transaction_screen.component
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
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//import java.util.Calendar
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TransactionForm(
//    selectedTransaction: TabButton,
//    titleFieldValue: TextFieldValue,
//    onTitleChange: (String) -> Unit,
//    transactionFieldValue: TextFieldValue,
//    onTransactionChange: (String) -> Unit,
//    funds: List<Fund>,
//    participantByFundId: Map<Int, List<Participant>>,
//    selectedFund: Fund?,
//    onFundSelected: (Fund) -> Unit,
//    selectedPar: Participant?,
//    onParSelected: (Participant) -> Unit,
//    selectedDate: String,
//    onDateChange: (String) -> Unit,
//    category: String,
//    onSave: () -> Unit
//) {
//    val openDialog = remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize(),
//        verticalArrangement = Arrangement.SpaceBetween
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Button(onClick = {
//                    openDialog.value = true
//                }) {
//                    Text(text = selectedDate)
//                }
//            }
//
//            if (openDialog.value) {
//                ChooseDate(
//                    onDismiss = {
//                        openDialog.value = false
//                    },
//                    onConfirm = { date ->
////                        onDateChange(date)
//                    }
//                )
//            }
//
//            TabTransactionType(transactionId = 0)
//            Spacer(modifier = Modifier.padding(5.dp))
//            TextField(
//                value = titleFieldValue,
//                onValueChange = { field ->
//                    onTitleChange(field.text)
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(5.dp),
//                maxLines = 1,
//                singleLine = true,
//                placeholder = {
//                    Text(
//                        text = if (selectedTransaction == TabButton.INCOME)
//                            "Income title"
//                        else "Expense title",
//                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600)
//                    )
//                },
//                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600),
//                colors = TextFieldDefaults.textFieldColors(
//                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
//                    cursorColor = MaterialTheme.colorScheme.primary,
//                    containerColor = Color.LightGray
//                )
//            )
//            Spacer(modifier = Modifier.padding(5.dp))
//            Divider(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 5.dp)
//            )
//            Spacer(modifier = Modifier.padding(5.dp))
//            FundDropdownMenu(
////                funds = funds,
////                selectedFund = selectedFund,
//                onFundSelected = onFundSelected
//            )
//            Spacer(modifier = Modifier.padding(5.dp))
//
//            selectedFund?.let { fund ->
//                participantByFundId[fund.fundId]?.let { participants ->
//                    participants.forEach { participant ->
//                        ParDropdownMenu(
////                            selectedParticipant = selectedPar,
//                            onParSelected = onParSelected
//                        )
//                    }
//                }
//            }
//            Text(
//                text = if (selectedTransaction == TabButton.INCOME) {
//                    "This is an Income: "
//                } else "This is an Expense: ",
//                style = MaterialTheme.typography.labelLarge,
//                color = MaterialTheme.colorScheme.onSurface,
//                modifier = Modifier
//                    .padding(horizontal = 5.dp, vertical = 5.dp)
//                    .align(Alignment.Start)
//            )
//            TextField(
//                value = transactionFieldValue,
//                onValueChange = { field ->
//                    onTransactionChange(field.text)
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(5.dp),
//                maxLines = 1,
//                singleLine = true,
//                placeholder = {
//                    Text(
//                        text = "Enter the amount"
//                    )
//                },
//                textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W600),
//                colors = TextFieldDefaults.textFieldColors(
//                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
//                    cursorColor = MaterialTheme.colorScheme.primary,
//                    containerColor = Color.LightGray
//                ),
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Number,
//                    imeAction = ImeAction.Done
//                )
//            )
//            Spacer(modifier = Modifier.padding(5.dp))
//            Text(
//                text = "Set category"
//            )
//            Category(transId = 0)
//        }
//        Button(
//            onClick = onSave,
//            shape = MaterialTheme.shapes.small,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .align(Alignment.CenterHorizontally)
//        ) {
//            Text(text = "Save")
//        }
//    }
//}
