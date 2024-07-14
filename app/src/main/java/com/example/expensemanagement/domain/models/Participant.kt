package com.example.expensemanagement.domain.models

data class Participant(
    val participantId: Int,
    val income: Double,
    val expense: Double,
    val balance: Double,
    val fundId: Int
)
