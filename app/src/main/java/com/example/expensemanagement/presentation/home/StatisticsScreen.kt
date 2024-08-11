package com.example.expensemanagement.presentation.home

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensemanagement.presentation.common.TabContent
import com.example.expensemanagement.presentation.home.component.FundBarChart
import com.example.expensemanagement.presentation.home.component.ParBarChart
import com.example.expensemanagement.presentation.home.component.TabBar
import kotlinx.coroutines.launch

@Composable
fun StatisticsScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val selectedTab by homeViewModel.tabPaid.collectAsState()
    val fundExpenseUnPaid by homeViewModel.fundExpenseUnPaid.collectAsState()
    val parExpenseUnPaid by homeViewModel.parExpenseUnPaid.collectAsState()
    val fundAndExpense by homeViewModel.fundAndExpense.collectAsState()
    val parAndExpense by homeViewModel.parAndExpense.collectAsState()

    LaunchedEffect(Unit){
        launch { homeViewModel.fetchFundAndExpense() }
        launch { homeViewModel.fetchParAndExpense() }
        launch { homeViewModel.fetchFundExpenseUnPaid() }
        launch { homeViewModel.fetchParExpenseUnPaid() }
    }

    Log.d("fundExpenseUnPaid", fundExpenseUnPaid.toString())
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabBar(
            tab1 = TabContent.ALLTRANSACTIONS,
            tab2 = TabContent.UNPAID,
            selectedTab = selectedTab
        ) {tabContent ->
            Log.d("tabContent", tabContent.title)
            homeViewModel.setTabPaid(tabContent)
        }
        val fundExpense = fundAndExpense.map { (fund, expense) ->
            fund to homeViewModel.formatAndScaleValue(expense)
        }
        val parExpense = parAndExpense.map { (par, expense) ->
            par to homeViewModel.formatAndScaleValue(expense)
        }
        val fundUnpaid = fundExpenseUnPaid.map { (fund, expense) ->
            fund to homeViewModel.formatAndScaleValue(expense)
        }
        val parUnpaid = parExpenseUnPaid.map { (par, expense) ->
            par to homeViewModel.formatAndScaleValue(expense)
        }

        if (fundUnpaid.isNotEmpty() && fundExpense.isNotEmpty()) {
            AnimatedContent(targetState = selectedTab) { targetTab ->
                when (targetTab) {
                    TabContent.ALLTRANSACTIONS -> FundBarChart(
                        oxLabel = "Fund",
                        oyLabel = "Money",
                        chartData = fundExpense
                    )
                    else -> FundBarChart(
                    oxLabel = "Fund",
                    oyLabel = "Money",
                    chartData = fundUnpaid
                )
                }
            }
        }
        Spacer(modifier = Modifier.padding(10.dp))
        if (parExpense.isNotEmpty() && parUnpaid.isNotEmpty()) {
            AnimatedContent(targetState = selectedTab) { targetTab ->
                when (targetTab) {
                    TabContent.ALLTRANSACTIONS -> ParBarChart(
                        oxLabel = "Participant",
                        oyLabel = "Money",
                        chartData = parExpense
                    )
                    else -> ParBarChart(
                        oxLabel = "Participant",
                        oyLabel = "Money",
                        chartData = parUnpaid
                    )
                }
            }
        }

    }
}