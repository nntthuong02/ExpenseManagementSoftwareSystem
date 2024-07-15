package com.example.expensemanagement.domain.usecase.read_datastore

import com.example.expensemanagement.domain.repository.DatastoreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrencyUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository
) {
    suspend operator fun invoke(): Flow<String>{
        return datastoreRepository.readCurrency()
    }
}