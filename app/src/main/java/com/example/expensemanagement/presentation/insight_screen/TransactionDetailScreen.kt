package com.example.expensemanagement.presentation.insight_screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.presentation.home.component.CenterAlignedTopAppBar
import com.example.expensemanagement.presentation.insight_screen.component.TransactionItem
import com.example.expensemanagement.presentation.insight_screen.component.getCategory
import com.example.expensemanagement.presentation.navigation.Route

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionDetailScreen(
    participantId: Int,
    fundId: Int,
    navController: NavHostController,
    insightViewModel: InsightViewModel = hiltViewModel()
) {
    val currencyCode by insightViewModel.selectedCurrencyCode.collectAsState()
    //transactionById is empty
    val transByFundAndPar by insightViewModel.transByFundAndPar.collectAsState()
    //participantName is null
    if (participantId != null) {
        insightViewModel.getTransactionByParAndFund(fundId, participantId)
    }

    CenterAlignedTopAppBar(
        showSnackbarText = "",
        name = "List Transactions",
        rightIcon1 = 0,
        rightIcon2 = 0,
        iconOnclick1 = { /*TODO*/ },
        iconOnlick2 = { /*TODO*/ },
        showIconRight1 = false,
        showIconRight2 = false,
        showIconLeft = true,
        navController = navController,
        showSnackbar = mutableStateOf(false)
    ) { innerPadding ->
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .padding(
                    top = 5.dp
                )
                .padding(paddingValues = innerPadding)
        ) {
            transByFundAndPar.ifEmpty {
                Box(
                    modifier = Modifier
                        .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                        .padding(0.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Not Transaction",
                        modifier = Modifier

                            .align(Alignment.Center)
                            .padding(8.dp),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(
                    start = 5.dp,
                    top = 5.dp,
                    end = 5.dp
                )
            ) {

                transByFundAndPar.forEach { (date, transactionList) ->
                    stickyHeader {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.background
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
                        val category = getCategory(transaction.category)
                        TransactionItem(
                            category = category,
                            currencyCode = currencyCode,
                            transaction = transaction,
                            onItemClick = { transId ->
                                navController.navigate("${Route.EditTransactionScreen.route}/$transId?fundId=${fundId}")
                            }
                        )
                    }
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun TransactionDetailScreenPreview() {
    TransactionDetailScreen(navController = rememberNavController(), participantId = 1, fundId = 1)
}