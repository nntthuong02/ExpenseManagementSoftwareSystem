package com.example.expensemanagement.domain.usecase.read_database

import com.example.expensemanagement.data.local.entity.FundDto
import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFundByGroupId @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    operator fun invoke(groupId: Int): Flow<List<FundDto>>
    {
        return databaseRepository.getFundByGroupId(groupId)
    }
}