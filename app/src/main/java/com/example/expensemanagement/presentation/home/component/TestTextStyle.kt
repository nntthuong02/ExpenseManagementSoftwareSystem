package com.example.expensemanagement.presentation.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
    }
}

@Preview(showSystemUi = true)
@Composable
fun TestTextStylePreview() {
    TestTextStyle()
}