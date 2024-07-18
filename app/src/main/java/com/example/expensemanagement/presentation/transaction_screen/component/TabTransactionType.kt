package com.example.expensemanagement.presentation.transaction_screen.component

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensemanagement.presentation.common.TabButton
import com.example.expensemanagement.presentation.transaction_screen.TransactionViewModel

@Composable
fun TabTransactionType(
    tabs: Array<TabButton> = TabButton.values(),
    cornerRadius: Dp = 24.dp,
    onButtonClick: () -> Unit = { },
    transactionViewModel: TransactionViewModel = hiltViewModel()
) {
    val selectedTab by transactionViewModel.tabButton.collectAsState()
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
//            Log.d("TabButton", "ok")
            tabs.forEach { tab ->
                val backgroundColor by animateColorAsState(
                    if (selectedTab == tab) MaterialTheme.colorScheme.onSurface
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
    TabTransactionType()
}