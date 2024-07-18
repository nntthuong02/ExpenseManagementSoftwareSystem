package com.example.expensemanagement.domain.usecase.read_database

import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetParticipantByFundId @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    operator fun invoke(fundId: Int): Flow<List<ParticipantDto>>
    {
        return databaseRepository.getParticipantByFundId(fundId)
    }
}