package com.example.expensemanagement.presentation.payment_screen.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensemanagement.domain.models.Fund

@Composable
fun RowItem(
    name: String,
    expense: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier
            .weight(0.3f)
            .border(1.dp, Color.Black)
        ){
            Text(
                text = name,
                modifier = Modifier.padding(8.dp)
//                    .align(Alignment.Center)
            )
        }
        Box(modifier = Modifier
            .weight(0.7f)
            .border(1.dp, Color.Black)
        ){
            Text(
                text = expense.toString(),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RowItemPreview(){
    val fund = Fund(1, "Quy 2", 1)
    RowItem(name = fund.fundName, expense = "10000000.0")
}