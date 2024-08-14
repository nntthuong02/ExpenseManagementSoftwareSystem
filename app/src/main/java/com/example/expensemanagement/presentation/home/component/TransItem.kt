package com.example.expensemanagement.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.domain.models.Transaction
import com.example.expensemanagement.presentation.common.Category
import com.example.expensemanagement.presentation.home.HomeViewModel
import com.example.expensemanagement.presentation.insight_screen.InsightViewModel
import com.example.expensemanagement.presentation.insight_screen.component.getCategory
import com.example.expensemanagement.ui.theme.Blue1
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransItem(
    transaction: Transaction,
    category: Category,
    currencyCode: String,
    parName: String,
) {

    Card(
        onClick = {  },
        colors = CardDefaults.cardColors(Color.DarkGray.copy(alpha = 0.1f)),
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            color = if (transaction.transactionType == Constants.INCOME) Color.Green
                            else Color.Red.copy(alpha = 0.75f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(
                            vertical = 5.dp,
                            horizontal = 10.dp
                        ),
                    color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary),
                    letterSpacing = TextUnit(1.1f, TextUnitType.Sp)
                )
                Text(
                    text = parName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            color = Blue1,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(
                            vertical = 5.dp,
                            horizontal = 10.dp
                        ),
                    color = Color.White,
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
                    Spacer(modifier = Modifier.padding(5.dp))
                    Column(verticalArrangement = Arrangement.SpaceBetween) {
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
                            text = currencyCode + "  ${transaction.amount}",
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
        transactionType = Constants.INCOME,
        isPaid = false,
        participantId = 1,
        fundId = 1
    )
    TransItem(transaction, Category.CLOTHES, "VND", "Thuong")
}