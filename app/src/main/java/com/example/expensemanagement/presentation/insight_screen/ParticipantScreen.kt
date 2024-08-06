package com.example.expensemanagement.presentation.insight_screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.presentation.insight_screen.component.FundItem
import com.example.expensemanagement.presentation.insight_screen.component.ParticipantItem
import com.example.expensemanagement.presentation.navigation.Route
import com.example.expensemanagement.presentation.transaction_screen.TransactionViewModel


@Composable
fun ParticipantScreen(
    navController: NavHostController,
    fundId: Int,
    insightViewModel: InsightViewModel = hiltViewModel()
) {
//    Text(text = "ParticipantScreen")
    val parByFundId by insightViewModel.parByFund.collectAsState()
    val currency by insightViewModel.selectedCurrencyCode.collectAsState()
    val expenseAndPar by insightViewModel.parAndExpense.collectAsState()

    Log.d("fundId", fundId.toString())
    LaunchedEffect(parByFundId) {
        if (parByFundId.isNotEmpty()) {
            parByFundId[fundId]?.let { listPar ->
                insightViewModel.expenseByParAndFund(fundId, listPar)
            }
            Log.d("parByFundId", parByFundId.toString())
            Log.d("expenseAndPar", expenseAndPar.toString())
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "List Participant",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.W700),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 5.dp,
                                end = 5.dp,
                                top = 5.dp
                            ),
                        textAlign = TextAlign.Center
                    )
                }

            }
//            parByFundId.keys.forEach { key ->
//                if(key == fundId){
//                    val participants = parByFundId[key]
//                    if (participants != null){
//                        itemsIndexed(participants) { index, par ->
//                            ParticipantItem(par, fundId, currency) {parId ->
//                                navController.navigate("${Route.TransactionDetailScreen.route}/$parId?fundId=${fundId}")
//                            }
//                        }
//                    }
//                }
//            }
            itemsIndexed(expenseAndPar){index, (par, expense) ->
                ParticipantItem(participant = par, fundId = fundId, currency = currency, expense ){parId ->
                    navController.navigate("${Route.TransactionDetailScreen.route}/$parId?fundId=${fundId}")
                }
            }

        }
        //LazyColumn

    }
}

@Preview(showSystemUi = true)
@Composable
fun ParticipantScreenPreview(){
    ParticipantScreen(navController = rememberNavController(), 1)
}
