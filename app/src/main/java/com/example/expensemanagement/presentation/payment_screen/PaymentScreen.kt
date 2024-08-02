package com.example.expensemanagement.presentation.payment_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.presentation.common.TabContent
import com.example.expensemanagement.presentation.home.component.TabBar
import com.example.expensemanagement.presentation.payment_screen.component.RowItem

@Composable
fun PaymentScreen() {

    val selectedTab = TabContent.PAYMENT
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabBar(tab1 = TabContent.PAYMENT, tab2 = TabContent.HISTORY, selectedTab = selectedTab) {
        }
        AnimatedContent(targetState = selectedTab) { targetTab ->
            when (targetTab) {
                TabContent.PAYMENT -> PaymentContent()
                else -> HistoryPayment()
            }

        }
    }
}

@Composable
fun PaymentContent() {
    val item1 = (1..5).toList()
    val item2 = (1..5).toList()
    val listFund = listOf(
        Fund(1, "Quy 2", 1),
        Fund(1, "Quy 2", 1),
        Fund(1, "Quy 2", 1),
        Fund(1, "Quy 2", 1),
    )
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.weight(0.5f)
        ) {
            Box(
                modifier = Modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
//                        .background(Color.LightGray) // Màu nền cho văn bản
                    .padding(0.dp) // Khoảng cách giữa văn bản và viền nền
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Fund Fee Schedule",
                    modifier = Modifier

                        .align(Alignment.Center)
                        .padding(8.dp), // Tạo khoảng cách giữa văn bản và đường viền
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            LazyColumn{
                item {
                    RowItem(name = "Fund", expense = "Expense")
                }
                itemsIndexed(listFund){index, fund ->
                    RowItem(name = fund.fundName, expense = fund.groupId.toString())
                }
            }
        }
        Row(
            modifier = Modifier.weight(0.5f)
        ) {
            Spacer(modifier = Modifier.padding(top = 5.dp))
            Box(
                modifier = Modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
//                        .background(Color.LightGray) // Màu nền cho văn bản
                    .padding(0.dp) // Khoảng cách giữa văn bản và viền nền
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Payment Schedule",
                    modifier = Modifier

                        .align(Alignment.Center)
                        .padding(8.dp), // Tạo khoảng cách giữa văn bản và đường viền
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            LazyColumn{
                item {
                    RowItem(name = "Participant", expense = "Payment Amount")
                }
                itemsIndexed(listFund){index, fund ->
                    RowItem(name = fund.fundName, expense = fund.groupId.toString())
                }
            }
        }
    }

//    LazyVerticalGrid(
//        columns = GridCells.Fixed(2), // Số cột cố định
//        modifier = Modifier.padding(16.dp)
//    ) {
//        items(item1.size) { item ->
//            Box(modifier = Modifier
//                .border(1.dp, Color.Black)
//            ){
//                Text(
//                    text = "Item ${item1[item]}",
//                    modifier = Modifier.padding(8.dp)
//                )
//            }
//
//        }
//    }
}

@Composable
fun HistoryPayment() {
    Text("HistoryPayment")
}

@Preview(showBackground = true)
@Composable
fun PreviewPaymentContent() {
    PaymentContent()
}