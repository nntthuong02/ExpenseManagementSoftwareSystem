@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.expensemanagement.presentation.insight_screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensemanagement.domain.models.Transaction
import com.example.expensemanagement.presentation.insight_screen.InsightViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.presentation.common.Category
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionItem(
    transaction: Transaction,
    category: Category,
    currencyCode: String,
//    insightViewModel: InsightViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit
) {
//    val category = getCategory(transaction.category)
//    val currencyCode by insightViewModel.selectedCurrencyCode.collectAsState()
    Card(
        onClick = { onItemClick(transaction.transactionId) },
        colors = CardDefaults.cardColors(Color.DarkGray.copy(alpha = 0.1f)),
//        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 5.dp,
                    vertical = 5.dp
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            category.bgRes,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(
                            vertical = 5.dp,
                            horizontal = 10.dp
                        ),
                    color = category.colorRes,
                    letterSpacing = TextUnit(1.1f, TextUnitType.Sp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(0.7f)
                ) {
                    Icon(
                        painter = painterResource(id = category.iconRes),
                        contentDescription = "transaction",
                        tint = Color.Black,
                        modifier = Modifier
                            .background(
                                Color.DarkGray.copy(alpha = 0.2f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(18.dp)
                    )


                    Column(
                        Modifier.padding(start = 5.dp),
                        verticalArrangement = Arrangement.SpaceBetween,
                       ) {
                        if (transaction.title.isNotEmpty()) {
                            Text(
                                text = transaction.title,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(5.dp))
                        }

                        Text(
                            text = "$currencyCode  " + formatAmount(transaction.amount),
                            color = if (transaction.transactionType == Constants.INCOME) Color.Green
                            else Color.Red.copy(alpha = 0.75f),
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.W600),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(0.3f)
                ){
                    Text(
                        text = if (transaction.isPaid) "Paid" else "Unpaid",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (transaction.isPaid) Color.Green else Color.Red,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
    Card(
        colors = CardDefaults.cardColors(Color.DarkGray.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
    ) {
    }
}

private fun formatAmount(value: Double): String {
    if (value == 0.0) {
        return "0,0"
    }
    val symbols = DecimalFormatSymbols(Locale.US).apply {
        decimalSeparator = ','
        groupingSeparator = '.'
    }
    val format = DecimalFormat("#,###.0", symbols)

    return format.format(value)
}
fun getCategory(title: String): Category {
    var result: Category = Category.FOOD_DRINK
    Category.values().forEach {
        if (it.title == title)
            result = it
    }
    return result
}

@Preview(showSystemUi = true)
@Composable
fun TransactionItemPreview(){
    val transaction = Transaction(
        transactionId = 1,
        date = Date(),
        dateOfEntry = "Test",
        category = "FOOD_DRINK",
        title = "Grocery shopping",
        amount = 50.0,
        transactionType = Constants.EXPENSE,
        isPaid = false,
        participantId = 1,
        fundId = 1
    )
    TransactionItem(
        currencyCode = "VND",
        category = Category.CLOTHES,
        transaction = transaction,
        onItemClick = {}
    )
}