package com.example.expensemanagement.domain.usecase.read_database


import com.example.expensemanagement.data.local.entity.TransactionDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransIsNotPaidById @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    operator fun invoke(transId: Int): Flow<List<TransactionDto>> {
        return databaseRepository.getTransIsNotPaidById(transId)
    }
}