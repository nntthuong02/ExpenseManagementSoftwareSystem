package com.example.expensemanagement.domain.usecase.write_database

import com.example.expensemanagement.domain.repository.DatabaseRepository
import javax.inject.Inject

class EraseParFundById @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(parFundId: Int){
        databaseRepository.eraseParFundById(parFundId)
    }
}