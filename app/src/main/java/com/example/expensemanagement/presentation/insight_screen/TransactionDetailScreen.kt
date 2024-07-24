package com.example.expensemanagement.presentation.insight_screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.presentation.insight_screen.component.TransactionItem
import com.example.expensemanagement.presentation.navigation.Route

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionDetailScreen(
    participantId: Int,
    fundId: Int,
    navController: NavHostController,
    insightViewModel: InsightViewModel = hiltViewModel()
) {
    //transactionById is empty
    val transactionsByParId by insightViewModel.transactionByParId.collectAsState()
    //participantName is null
    if (participantId != null) {
        insightViewModel.getTransaction(participantId)
        Log.d("test participantName", "$participantId")
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.padding(
            top = 5.dp
        )
    ) {
        transactionsByParId.ifEmpty {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Not Transaction",
                    textAlign = TextAlign.Center
                    )
                Log.d("test transactionById", "isEmpty")
            }

        }

        LazyColumn(
            contentPadding = PaddingValues(
                start = 5.dp,
                top = 5.dp,
                end = 5.dp
            )
        ) {
            item {
                Text(
                    text = "Transactions",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Normal),
                )

            }
            Log.d("test ParticipantDetail", "test1")
            transactionsByParId.forEach { (date, transactionList) ->
                Log.d("test ParticipantDetail", "test2")
                stickyHeader {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(
                                vertical = 5.dp
                            ),
                    ) {
                        Text(
                            text = date,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    }
                }

                itemsIndexed(transactionList) { index, transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onItemClick = {transId ->
                            navController.navigate("${Route.EditTransactionScreen.route}/$transId?fundId=${fundId}")
                        }
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TransactionDetailScreenPreview(){
    TransactionDetailScreen(navController = rememberNavController(), participantId = 1, fundId = 1)
}