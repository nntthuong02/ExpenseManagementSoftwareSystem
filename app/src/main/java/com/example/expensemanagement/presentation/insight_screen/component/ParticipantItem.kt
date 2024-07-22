package com.example.expensemanagement.presentation.insight_screen.component

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
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.ui.theme.Abigail
import com.example.expensemanagement.ui.theme.Adonis

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticipantItem(
    participant: Participant,
    currency: String,
    onItemClick: (String) -> Unit
) {
    Card(
        onClick = {
            onItemClick(participant.participantName)
        },
        colors = CardDefaults.cardColors(Color.DarkGray.copy(alpha= 0.1f)),
//       elevation = CardDefaults.cardElevation(5.dp),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 5.dp,
                start = 5.dp,
                end = 5.dp
            )
    ) {
        //Balance
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 5.dp
                )
        ) {
            Text(
                text = "Balance",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(
                    start = 5.dp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 10.sp,
                        letterSpacing = 0.2.sp
                    )
                ){
                    append(currency)
                }
                withStyle(
                    SpanStyle(
                        fontWeight = FontWeight.ExtraLight
                    )
                ){
                    append(participant.balance.toString())
                }
            },
                modifier = Modifier.padding(start = 5.dp)
            )
            //Balance
            //Participant
//            val color = when(participant.participant){
//                ParticipantName.DAT.title -> ParticipantName.DAT.color
//                ParticipantName.QUAN.title -> ParticipantName.QUAN.color
//                ParticipantName.TUAN.title -> ParticipantName.THUONG.color
//                else -> ParticipantName.TUAN.color
//            }

            Surface(
                color = Color.Gray.copy(alpha= 0.1f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(verticalArrangement = Arrangement.Center) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = participant.participantName,
                            style = MaterialTheme.typography.titleMedium,
                            color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.onSurface)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                    ) {
                        Text(text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.4.sp,
                                    color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.onSurface)
                                )
                            ){
                                append(currency + "  ")
                            }
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Thin,
                                    fontSize = 14.sp,
                                    letterSpacing = 0.2.sp,
                                    color = contentColorFor(backgroundColor = Color.Red)
                                )
                            ){
                                append(participant.income.toString())
                            }


                        })
                        Text(text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Thin,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.4.sp,
                                    color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.onSurface)
                                )
                            ) {
                                append(currency + "  ")
                            }
                            withStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Thin,
                                    fontSize = 14.sp,
                                    letterSpacing = 0.2.sp,
                                    color = contentColorFor(backgroundColor = MaterialTheme.colorScheme.onSurface)
                                )
                            ) {
                                append(participant.expense.toString())
                            }
                        })

                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp)
                    ) {
                        Text(
                            text = "INCOME",
                            style = MaterialTheme.typography.titleMedium,
                            color = contentColorFor(backgroundColor = Adonis)
                        )

                        Text(
                            text = "EXPENSE",
                            style = MaterialTheme.typography.titleMedium,
                            color = contentColorFor(backgroundColor = Color.Red)
                        )
                    }
                }
            }
            //Participant
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ParticipantItemPreview(){
    val thuong = Participant(1, "Thuong", 0.0, 0.0, 0.0)
    ParticipantItem(participant = thuong, currency = "VND", onItemClick = {})
}