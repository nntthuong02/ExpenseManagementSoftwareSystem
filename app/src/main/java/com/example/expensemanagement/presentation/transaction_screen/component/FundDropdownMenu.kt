package com.example.expensemanagement.presentation.transaction_screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.presentation.transaction_screen.TransactionViewModel


@Composable
fun FundDropdownMenu(
    funds: List<Fund>,
    onFundSelected: (Fund) -> Unit,

    transactionViewModel: TransactionViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedFund by remember { mutableStateOf<Fund?>(null) }
//    val funds by transactionViewModel.fundByGroupId.collectAsState()
    LaunchedEffect(funds) {
        if (funds.isNotEmpty()) {
            selectedFund = funds.first() // Chọn Fund đầu tiên làm mặc định
            onFundSelected(selectedFund!!)
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { expanded = !expanded }) {
            Text(text = selectedFund?.fundName ?: "Select Fund")
        }
        DropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            funds.forEach { fund ->
                DropdownMenuItem(
                    text = { Text(fund.fundName) },
                    onClick = {
                    selectedFund = fund
                    expanded = false
                    onFundSelected(fund)
                    }
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun FundDropMenuPreview(){
    val listFund = listOf(
        Fund(1, "QUy", 1)
    )
    FundDropdownMenu(funds = listFund, onFundSelected = {})
}