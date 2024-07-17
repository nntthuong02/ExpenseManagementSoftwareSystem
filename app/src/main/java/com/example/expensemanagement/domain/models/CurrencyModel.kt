package com.example.expensemanagement.domain.models

data class CurrencyModel(
    val country: String = String(),
    val currencyCode: String = String(),
    val currencySymbol: String = String()
)
