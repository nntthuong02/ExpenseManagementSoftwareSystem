package com.example.expensemanagement.domain.models

data class Participant(
    val participantId: Int,
    val participantName: String,
    val income: Double,
    val expense: Double,
    val balance: Double,
)
