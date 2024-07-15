package com.example.expensemanagement.domain.usecase.write_database

import com.example.expensemanagement.domain.repository.DatabaseRepository
import javax.inject.Inject

class MarkAllAsPaid @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(){
        databaseRepository.markAllAsPaid()
    }
}