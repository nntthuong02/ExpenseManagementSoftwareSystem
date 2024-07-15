package com.example.expensemanagement.domain.repository

import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.data.local.entity.TransactionDto
import kotlinx.coroutines.flow.Flow


interface DatabaseRepository {
    suspend fun insertTransaction(trans: TransactionDto)

    suspend fun insertParticipant(participant: ParticipantDto)

    suspend fun markAllAsPaid()

    fun getTransactionByDate(entryDate: String): Flow<List<TransactionDto>>

    fun getTransactionByParticipant(participantId: Int): Flow<List<TransactionDto>>

    fun getTransIsNotPaidById(transId: Int): Flow<List<TransactionDto>>

    fun getTransByFund(fundId: Int): Flow<List<TransactionDto>>

    fun getTransByFundAndPar(fundId: Int, parId: Int): Flow<List<TransactionDto>>

    fun getParticipantByName(participantName: String): Flow<ParticipantDto>
    fun getAllParticipants(): Flow<List<ParticipantDto>>

    fun getAllTransactions(): Flow<List<TransactionDto>>

    fun eraseTransaction()

    fun getCurrentDayTransaction(): Flow<List<TransactionDto>>

    fun getWeeklyTransaction(): Flow<List<TransactionDto>>

    fun getMonthlyTransaction(): Flow<List<TransactionDto>>

    fun getTransactionByType(transactionType: String): Flow<List<TransactionDto>>
}