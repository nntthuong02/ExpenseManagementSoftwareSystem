package com.example.expensemanagement.domain.usecase.read_database

import com.example.expensemanagement.data.local.entity.GroupDto
import com.example.expensemanagement.data.local.entity.ParticipantFundDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetParticipantFundById @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    operator fun invoke(parFundId: Int): Flow<ParticipantFundDto>
    {
        return databaseRepository.getParticipantFundById(parFundId)
    }
}