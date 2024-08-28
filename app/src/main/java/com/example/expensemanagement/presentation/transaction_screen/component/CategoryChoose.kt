package com.example.expensemanagement.presentation.transaction_screen.component

import android.util.Log
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensemanagement.R
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.presentation.common.Category
import com.example.expensemanagement.presentation.common.TabButton
import com.example.expensemanagement.presentation.insight_screen.InsightViewModel
import com.example.expensemanagement.presentation.transaction_screen.TransactionViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Category(
    expenseItems: Array<Category> = Category.values(),
) {
    FlowRow(
        modifier = Modifier.padding(
            start = 5.dp,
            top = 5.dp,
            bottom = 5.dp,
        ),
    ) {
        expenseItems.forEach {
            CategoryTag(category = it)
        }
    }
}

@Composable
fun CategoryTag(
    category: Category,
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    insightViewModel: InsightViewModel = hiltViewModel()
) {
    val selected by transactionViewModel.category.collectAsState()
    var isSelected = selected.title == category.title

    TextButton(
        modifier = Modifier.padding(end = 5.dp),
        onClick = {
            transactionViewModel.selectCategory(category)
            isSelected = selected.title == category.title
        },
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(
            horizontal = 5.dp,
            vertical = 5.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
                category.bgRes.copy(alpha = 0.95f)
            } else MaterialTheme.colorScheme.surface,
            contentColor = if (isSelected) {
                category.colorRes
            } else MaterialTheme.colorScheme.onSurface
        ),
    ) {
        Icon(
            painter = if (!isSelected) {
                painterResource(id = R.drawable.add_24px)
            } else painterResource(id = category.iconRes),
            contentDescription = category.title,
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = category.title,
            style = MaterialTheme.typography.titleSmall
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CategoryPreview() {
    Category()
}