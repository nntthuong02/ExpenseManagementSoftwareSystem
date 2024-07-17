package com.example.expensemanagement.presentation.onboarding

import android.content.res.Resources
import androidx.annotation.DrawableRes
import com.example.expensemanagement.R

data class Page(
    val title: String,
    val description: String,
    @DrawableRes val image: Int
)
val pages = listOf(
    Page(
        title = "Expense Management",
        description = "Monitor spending, categorize expenses, and stay on budget",
        image = R.drawable.onboarding1
    ),
    Page(
        title = "Uncontrolled Spending",
        description = "The burden of overspending due to poor management",
        image = R.drawable.onboarding2
    ),
    Page(
        title ="Budget Management",
        description = "Take control of your finances effortlessly and allocate funds with ease",
        image = R.drawable.onboarding3
    )
)

