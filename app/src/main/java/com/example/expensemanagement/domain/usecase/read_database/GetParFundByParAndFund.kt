package com.example.expensemanagement.domain.usecase.read_database

import com.example.expensemanagement.data.local.entity.ParticipantFundDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetParFundByParAndFund @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    operator fun invoke(parId: Int, fundId: Int): Flow<ParticipantFundDto> {
        return databaseRepository.getParFundByParAndFund(parId, fundId)
    }
}