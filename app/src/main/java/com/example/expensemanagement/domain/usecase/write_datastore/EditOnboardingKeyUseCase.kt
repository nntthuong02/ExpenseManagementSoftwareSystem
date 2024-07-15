package com.example.expensemanagement.domain.usecase.write_datastore

import com.example.expensemanagement.domain.repository.DatastoreRepository
import javax.inject.Inject

class EditOnboardingKeyUseCase @Inject constructor(
    private val datastoreRepository: DatastoreRepository
) {
    suspend operator fun invoke(completed: Boolean){
        datastoreRepository.writeOnboardingKey(completed)
    }
}