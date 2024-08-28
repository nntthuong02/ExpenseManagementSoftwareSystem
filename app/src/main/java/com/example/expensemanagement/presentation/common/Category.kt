package com.example.expensemanagement.presentation.common

import androidx.compose.ui.graphics.Color
import com.example.expensemanagement.R

enum class Category(val title: String, val iconRes: Int, val colorRes: Color, val bgRes: Color) {
    FOOD_DRINK("food_drink", R.drawable.food, Color.Blue, Color.Yellow),
    HEALTH("health", R.drawable.health_and_safety_24px, Color.Blue, Color.Yellow),
    WATER_ELECTRICITY("water_electricity", R.drawable.water_electricity, Color.Blue, Color.Yellow),
    EDUCATION("education", R.drawable.education, Color.Blue, Color.Yellow),
    CLOTHES("clothes", R.drawable.clothes, Color.Blue, Color.Yellow),
    TRAVEL("travel", R.drawable.travel, Color.Blue, Color.Yellow);

    companion object {
        fun fromTitle(title: String): Category? {
            return values().find { it.title == title }
        }
    }
}