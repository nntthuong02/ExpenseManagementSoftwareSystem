package com.example.expensemanagement.domain.models

import java.util.Date

data class Transaction(
    val transactionId: Int,
    val title: String,
    val date: Date,
    val dateOfEntry: String,
    val amount: Double,
    val category: String,
    val isPaid: Boolean,
    val transactionType: String,
    val participantId: Int,
    val fundId: Int
)
