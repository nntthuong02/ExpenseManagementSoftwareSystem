package com.example.expensemanagement.domain.usecase.write_database

import com.example.expensemanagement.data.local.entity.TransactionDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import javax.inject.Inject

class InsertNewTransaction @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(trans: TransactionDto){
        databaseRepository.insertTransaction(trans)
    }
}