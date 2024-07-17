package com.example.expensemanagement.domain.usecase.read_database

import com.example.expensemanagement.data.local.entity.FundDto
import com.example.expensemanagement.data.local.entity.GroupDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFundById @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    operator fun invoke(fundId: Int): Flow<FundDto>
    {
        return databaseRepository.getFundById(fundId)
    }
}