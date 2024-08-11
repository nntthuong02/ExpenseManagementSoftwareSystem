package com.example.expensemanagement.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.expensemanagement.R


@Composable
fun CardWithIcon(
    nameEntity: String,
    number: String,
    idIcon: Int,
    itemOnClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .padding(top = 30.dp)
            .width(150.dp)
            .height(100.dp)
    ) {
        Card(
            onClick = itemOnClick,
            modifier = Modifier
                .width(145.dp)
                .height(95.dp),
            colors = CardDefaults.cardColors(Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = nameEntity,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Serif
                    )
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = number,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Thin,
                        fontFamily = FontFamily.Serif
                    )
                )
            }
        }
        Box(
            modifier = Modifier
                .offset(y = (-18).dp)
                .size(32.dp)
                .zIndex(1f)
                .shadow(8.dp, shape = CircleShape)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
        Icon(
            painterResource(id = idIcon),
            contentDescription = "Icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(32.dp)
        )
    }
    }
}
@Preview(showSystemUi = true)
@Composable
fun CardWithIconPreview(){
    CardWithIcon("Thuong", "5", R.drawable.home_24px, {})
}