package com.example.expensemanagement.presentation.insight_screen

import android.util.Log
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.presentation.insight_screen.component.FundItem
import com.example.expensemanagement.presentation.navigation.Route
import com.example.expensemanagement.presentation.transaction_screen.TransactionViewModel


@Composable
fun InsightScreen(
    navController: NavHostController,
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    insightViewModel: InsightViewModel = hiltViewModel()
) {
//    Text(text = "InsightScreen")
    val funds by insightViewModel.fundByGroup.collectAsState()
    val parByFundId by insightViewModel.parByFund.collectAsState()
    val currency by insightViewModel.selectedCurrencyCode.collectAsState()

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Log.d("InsightScreen Test", " ok")
        LazyColumn {
            item {
                Text(
                    text = "List Fund",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W700),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 5.dp,
                            end = 5.dp,
                            top = 5.dp
                        ),
                    textAlign = TextAlign.Start
                )
            }
            itemsIndexed(funds) { index, fund ->
                FundItem(fund, currency) {fundId ->
                    Log.d("test lazyColumn", "${fund.fundId}")
                    navController.navigate("${Route.ParticipantScreen.route}/$fundId")
                    Log.d("test Route.ParticipantScreen", "${fundId}")
                }
            }
        }
        //LazyColumn

    }
}

@Preview(showSystemUi = true)
@Composable
fun InsightScreenPreview(){
    InsightScreen(navController = rememberNavController())
}
