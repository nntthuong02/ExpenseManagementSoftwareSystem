package com.example.expensemanagement.presentation.insight_screen.component

import android.util.Log
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.presentation.insight_screen.InsightViewModel
import com.example.expensemanagement.ui.theme.AcidLime
import com.example.expensemanagement.ui.theme.Adonis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundItem(
    fund: Fund,
    fundAmount: Double,
    currency: String,
//    insightViewModel: InsightViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
) {

    Card(
        onClick = {
            onItemClick(fund.fundId)
        },
        colors = CardDefaults.cardColors(Color.DarkGray.copy(alpha= 0.1f)),
//        elevation = CardDefaults.cardElevation(1.dp),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 5.dp,
                start = 5.dp,
                end = 5.dp
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 5.dp
                )
        ) {


            Text(
                text = fund.fundName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(10.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Surface(
                color = Color.Blue.copy(0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.Center) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "EXPENSE",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    ) {
                        Text(text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Thin,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.4.sp,
                                    color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary)
                                )
                            ) {
                                append(currency + "    ")
                            }
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Thin,
                                    fontSize = 14.sp,
                                    letterSpacing = 0.2.sp,
                                    color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.primary)
                                )
                            ) {
                                append(fundAmount.toString())
                            }
                        })
                    }
                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun FundItemPreview(){
    val myFund = Fund(1, "Thuong", 1)
    FundItem(fund = myFund, 1.0, currency = "VND", onItemClick = {})
}