package com.example.expensemanagement.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.expensemanagement.R

@Composable
fun TestTextStyle() {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1/3f)) {
            Column {
                Text(
                    text = "displayLarge",
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "displayMedium",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "displaySmall",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "headlineLarge",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "headlineMedium",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }

        Row(modifier = Modifier.weight(1/3f)) {
            Column {
                Text(
                    text = "headlineSmall",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "titleLarge",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "titleMedium",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "titleSmall",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "bodyLarge",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }

        Row(modifier = Modifier.weight(1/3f)) {
            Column {
                Text(
                    text = "bodyMedium",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "bodySmall",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "labelLarge",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "labelMedium",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(5.dp)
                )
                Text(
                    text = "labelSmall",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
        Button(
            modifier = Modifier.size(40.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green // Màu nền của Button là màu xanh lá cây
            ),
            onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.east_24px),
                contentDescription = null,
//                tint = Color.Red, // Màu của Icon là màu đỏ
                modifier = Modifier
                    .size(32.dp) // Kích thước của Icon
            )

        }


    }

}

@Preview(showSystemUi = true)
@Composable
fun TestTextStylePreview() {
    TestTextStyle()
}

@Composable
fun TestFontFamily(){
    Column(Modifier.fillMaxSize()) {
        Text(text = "Monospace", fontFamily = FontFamily.Monospace)
        Text(text = "Cursive", fontFamily = FontFamily.Cursive)
        Text(text = "Serif", fontFamily = FontFamily.Serif)
        Text(text = "SansSerif", fontFamily = FontFamily.SansSerif)
        Text(text = "Default", fontFamily = FontFamily.Default)

    }
}

@Preview(showSystemUi = true)
@Composable
fun TestFontFamilyPreview() {
    TestFontFamily()
}