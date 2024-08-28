package com.example.expensemanagement.presentation.navigation.component

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.R
import com.example.expensemanagement.ui.theme.ExpenseManagementTheme

@ExperimentalAnimationApi
@Composable
fun BottomNavBar(
    items: List<NavBarItemHolder>,
    navController: NavController,
    itemClick: (NavBarItemHolder) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    Card(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                val selected = item.route == backStackEntry?.destination?.route
                NavBarItem(
                    item = item,
                    //Xu li trong MainScreen
                    onClick = { itemClick(item) },
                    selected = selected
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun BottomNavBarPreview() {
    val items = listOf(
        NavBarItemHolder("Home", R.drawable.home_24px, "home"),
        NavBarItemHolder("Transaction", R.drawable.transaction_24px, "insight"),
        NavBarItemHolder("Insight", R.drawable.insight_24, "participant"),
        NavBarItemHolder("Payment", R.drawable.payments_24px, "settings")
    )

    val navController = rememberNavController()

    ExpenseManagementTheme {
        BottomNavBar(
            items = items,
            navController = navController,
            itemClick = { /* Handle item click here */ }
        )
    }
}