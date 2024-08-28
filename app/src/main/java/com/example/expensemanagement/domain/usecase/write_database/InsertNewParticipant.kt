package com.example.expensemanagement.domain.usecase.write_database

import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import javax.inject.Inject

class InsertNewParticipant @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(participant: ParticipantDto){
        databaseRepository.insertParticipant(participant)
    }
}