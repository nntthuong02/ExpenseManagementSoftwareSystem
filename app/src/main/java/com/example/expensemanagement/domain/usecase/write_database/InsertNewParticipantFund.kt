package com.example.expensemanagement.domain.usecase.write_database

import com.example.expensemanagement.data.local.entity.FundDto
import com.example.expensemanagement.data.local.entity.ParticipantFundDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import javax.inject.Inject

class InsertNewParticipantFund @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(parFund: ParticipantFundDto) {
        databaseRepository.insertParticipantFund(parFund)
    }
}