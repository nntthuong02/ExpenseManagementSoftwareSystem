package com.example.expensemanagement.domain.usecase.write_database

import com.example.expensemanagement.domain.repository.DatabaseRepository
import java.util.Date
import javax.inject.Inject

class UpdateTransactionDetails @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(
        id: Int,
        title: String,
        date: Date,
        amount: Double,
        category: String,
        transactionType: String,
        parId: Int,
        fundId: Int
    ){
       databaseRepository.updateTransactionDetails(id, title, date, amount, category, transactionType, parId, fundId)
    }
}