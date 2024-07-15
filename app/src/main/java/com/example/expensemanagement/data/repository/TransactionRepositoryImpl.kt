package com.example.expensemanagement.data.repository

import com.example.expensemanagement.data.ParticipantDao
import com.example.expensemanagement.data.TransactionDao
import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.data.local.entity.TransactionDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val participantDao: ParticipantDao
): DatabaseRepository{
    override suspend fun insertTransaction(trans: TransactionDto) {
        transactionDao.insertTransaction(trans)
    }

    override suspend fun insertParticipant(participant: ParticipantDto) {
        participantDao.insertParticipant(participant)
    }

    override suspend fun markAllAsPaid() {
        transactionDao.markAllAsPaid()
    }

    override fun getTransactionByDate(entryDate: String): Flow<List<TransactionDto>> {
        return transactionDao.getTransactionByDate(entryDate)
    }

    override fun getTransactionByParticipant(participantId: Int): Flow<List<TransactionDto>> {
        return transactionDao.getTransactionByParticipant(participantId)
    }

    override fun getTransIsNotPaidById(transId: Int): Flow<List<TransactionDto>> {
        return transactionDao.getTransIsNotPaidById(transId)
    }

    override fun getTransByFund(fundId: Int): Flow<List<TransactionDto>> {
        return transactionDao.getTransByFund(fundId)
    }

    override fun getTransByFundAndPar(fundId: Int, parId: Int): Flow<List<TransactionDto>> {
        return transactionDao.getTransByFundAndPar(fundId, parId)
    }

    override fun getParticipantByName(participantName: String): Flow<ParticipantDto> {
        return participantDao.getParticipantByName(participantName)
    }

    override fun getAllParticipants(): Flow<List<ParticipantDto>> {
        return participantDao.getAllParticipants()
    }

    override fun getAllTransactions(): Flow<List<TransactionDto>> {
        return transactionDao.getAllTransaction()
    }

    override fun eraseTransaction() {
        return transactionDao.eraseTransaction()
    }

    override fun getCurrentDayTransaction(): Flow<List<TransactionDto>> {
        return transactionDao.getCurrentDayExpTransaction()
    }

    override fun getWeeklyTransaction(): Flow<List<TransactionDto>> {
        return transactionDao.getWeeklyExpTransaction()
    }

    override fun getMonthlyTransaction(): Flow<List<TransactionDto>> {
        return transactionDao.getMonthlyExpTransaction()
    }

    override fun getTransactionByType(transactionType: String): Flow<List<TransactionDto>> {
        return transactionDao.getTransactionByType(transactionType)
    }

}