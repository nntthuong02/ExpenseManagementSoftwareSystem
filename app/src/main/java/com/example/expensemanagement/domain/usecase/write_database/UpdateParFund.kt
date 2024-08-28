package com.example.expensemanagement.domain.usecase.write_database

import com.example.expensemanagement.data.local.entity.ParticipantFundDto
import com.example.expensemanagement.data.local.entity.TransactionDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import javax.inject.Inject

class UpdateParFund @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(parFund: ParticipantFundDto){
        databaseRepository.updateParticipantFund(parFund)
    }
}