package com.example.expensemanagement.presentation.home.component
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun TabbedContentScreen() {
    // State to manage which tab is selected
    var selectedTab by remember { mutableStateOf("Tab1") }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Row with buttons to switch between tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { selectedTab = "Tab1" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "Tab1") Color.Blue else Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Tab 1")
            }
            Button(
                onClick = { selectedTab = "Tab2" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTab == "Tab2") Color.Blue else Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Tab 2")
            }
        }

        // Content below changes based on selected tab
        when (selectedTab) {
//            "Tab1" -> TabContent1()
//            "Tab2" -> TabContent2()
        }
    }
}

//@Composable
//fun TabContent1() {
//    // Content for Tab 1
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp)
//    ) {
//        Text(text = "Content for Tab 1", style = MaterialTheme.typography.headlineMedium)
//        // Add more content here
//    }
//}
//
//@Composable
//fun TabContent2() {
//    // Content for Tab 2
//    Column(
//        modifier = Modifier.fillMaxSize().padding(16.dp)
//    ) {
//        Text(text = "Content for Tab 2", style = MaterialTheme.typography.headlineMedium)
//        // Add more content here
//    }
//}
