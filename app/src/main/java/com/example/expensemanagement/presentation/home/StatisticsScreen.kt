package com.example.expensemanagement.presentation.home

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensemanagement.presentation.home.component.FundBarChart

@Composable
fun StatisticsScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val fundExpenseUnPaid by homeViewModel.fundExpenseUnPaid.collectAsState()
    val parExpenseUnPaid by homeViewModel.parExpenseUnPaid.collectAsState()
    Log.d("fundExpenseUnPaid", fundExpenseUnPaid.toString())
    FundBarChart(oxLabel = "Fund", oyLabel = "Money(thousand)", chartData = fundExpenseUnPaid)
    Text(text = "StatisticsScreen")
}