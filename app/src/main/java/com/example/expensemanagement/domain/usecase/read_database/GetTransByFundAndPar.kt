package com.example.expensemanagement.domain.usecase.read_database

import com.example.expensemanagement.data.local.entity.TransactionDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransByFundAndPar @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    operator fun invoke(fundId: Int, parId: Int): Flow<List<TransactionDto>>{
        return databaseRepository.getTransByFundAndPar(fundId, parId)
    }
}