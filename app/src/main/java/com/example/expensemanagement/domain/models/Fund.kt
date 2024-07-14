package com.example.expensemanagement.domain.models

data class Fund(
    val fundId: Int,
    val fundName: String,
    val totalAmount: Double,
    val groupId: Int
)
