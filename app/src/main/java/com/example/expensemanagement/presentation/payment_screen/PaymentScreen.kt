package com.example.expensemanagement.presentation.payment_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PaymentScreen() {
    val items = (1..30).toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // Số cột cố định
        modifier = Modifier.padding(16.dp)
    ) {
        items(items.size) { item ->
            Text(
                text = "Item ${items[item]}",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyLazyVerticalGrid() {
    PaymentScreen()
}