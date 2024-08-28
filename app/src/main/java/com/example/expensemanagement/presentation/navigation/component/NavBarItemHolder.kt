package com.example.expensemanagement.presentation.navigation.component

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.expensemanagement.R
import com.example.expensemanagement.presentation.navigation.Route

data class NavBarItemHolder(
    val title: String,
    val icon: Int,
    val route: String
)

fun provideBottomNavItems() = listOf(
    NavBarItemHolder(
        "Home",
        R.drawable.home_24px,
        Route.HomeScreen.route
    ),
    NavBarItemHolder(
        "Transaction",
        R.drawable.transaction_24px,
        Route.TransactionScreen.route
    ),
    NavBarItemHolder(
        "Insight",
        R.drawable.list_alt_24px,
        Route.InsightScreen.route
    ),
    NavBarItemHolder(
        "Payment",
        R.drawable.payments_24px,
        Route.PayScreen.route
    ),
    NavBarItemHolder(
        "Setting",
        R.drawable.settings_24px,
        Route.SettingScreen.route
    )
)
