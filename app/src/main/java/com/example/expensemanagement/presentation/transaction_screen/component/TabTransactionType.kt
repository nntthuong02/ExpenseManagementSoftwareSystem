package com.example.expensemanagement.presentation.transaction_screen.component

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.presentation.common.TabButton
import com.example.expensemanagement.presentation.insight_screen.InsightViewModel
import com.example.expensemanagement.presentation.transaction_screen.TransactionViewModel

@Composable
fun TabTransactionType(
    tabs: Array<TabButton> = TabButton.values(),
    cornerRadius: Dp = 24.dp,
    onButtonClick: () -> Unit = { },
    transactionId: Int,
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    insightViewModel: InsightViewModel = hiltViewModel()
) {
    val selectedTab by transactionViewModel.tabButton.collectAsState()
    val transactionById by insightViewModel.transactionById.collectAsState()
    LaunchedEffect(transactionId) {
        if (transactionId != 0) {
            insightViewModel.getTransById(transactionId)
            transactionById?.let { trans ->
                if (trans.transactionType == Constants.INCOME) {
                    transactionViewModel.selectTabButton(TabButton.INCOME)
                } else {
                    transactionViewModel.selectTabButton(TabButton.EXPENSE)
                }
            }
        }
    }

    Surface(
        modifier = Modifier.padding(
            start = 5.dp,
            end = 5.dp,
            top = 5.dp
        ),
        color = Color.DarkGray.copy(alpha = 0.1f),
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 5.dp,
                    end = 5.dp
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val backgroundColor by animateColorAsState(
                    if (selectedTab == tab) {
                        when (tab) {
                            TabButton.EXPENSE -> Color.Red // hoặc màu sắc khác cho EXPENSE
                            TabButton.INCOME -> Color.Green // hoặc màu sắc khác cho INCOME
                            else -> Color.Transparent // màu mặc định hoặc màu khác nếu cần
                        }
                    }
                    else Color.Transparent
                )

                val textColor by animateColorAsState(
                    if (selectedTab == tab) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.onSurface,
                )
                TextButton(
                    onClick = {
                        transactionViewModel.selectTabButton(tab)
                        onButtonClick()
                    },
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(cornerRadius),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = backgroundColor,
                        contentColor = textColor
                    )
                ) {
                    Text(
                        text = tab.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(
                                horizontal = 5.dp,
                                vertical = 5.dp
                            )
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TabButtonPreview() {
    TabTransactionType(transactionId = 1)
}