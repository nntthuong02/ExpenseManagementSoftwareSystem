package com.example.expensemanagement.domain.usecase.write_datastore

import com.example.expensemanagement.domain.repository.DatastoreRepository
import javax.inject.Inject

class EditCurrencyUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository
) {
    suspend operator fun invoke(currency: String){
        datastoreRepository.writeCurrency(currency)
    }
}