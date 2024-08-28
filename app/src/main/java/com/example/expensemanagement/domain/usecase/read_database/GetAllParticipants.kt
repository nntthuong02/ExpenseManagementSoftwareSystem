package com.example.expensemanagement.domain.usecase.read_database

import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllParticipants @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    operator fun invoke(): Flow<List<ParticipantDto>>{
        return databaseRepository.getAllParticipants()
    }
}