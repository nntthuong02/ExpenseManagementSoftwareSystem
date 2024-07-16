package com.example.expensemanagement.data.repository

import com.example.expensemanagement.data.DatabaseDao
import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.data.local.entity.TransactionDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val databaseDao: DatabaseDao
): DatabaseRepository{
    override suspend fun insertTransaction(trans: TransactionDto) {
        databaseDao.insertTransaction(trans)
    }

    override suspend fun insertParticipant(participant: ParticipantDto) {
        databaseDao.insertParticipant(participant)
    }

    override suspend fun markAllAsPaid() {
        databaseDao.markAllAsPaid()
    }

    override fun getTransactionByDate(entryDate: String): Flow<List<TransactionDto>> {
        return databaseDao.getTransactionByDate(entryDate)
    }

    override fun getTransactionByParticipant(participantId: Int): Flow<List<TransactionDto>> {
        return databaseDao.getTransactionByParticipant(participantId)
    }

    override fun getTransIsNotPaidById(transId: Int): Flow<List<TransactionDto>> {
        return databaseDao.getTransIsNotPaidById(transId)
    }

    override fun getTransByFund(fundId: Int): Flow<List<TransactionDto>> {
        return databaseDao.getTransByFund(fundId)
    }

    override fun getTransByFundAndPar(fundId: Int, parId: Int): Flow<List<TransactionDto>> {
        return databaseDao.getTransByFundAndPar(fundId, parId)
    }

    override fun getParticipantByName(participantName: String): Flow<ParticipantDto> {
        return databaseDao.getParticipantByName(participantName)
    }

    override fun getAllParticipants(): Flow<List<ParticipantDto>> {
        return databaseDao.getAllParticipants()
    }

    override fun getAllTransactions(): Flow<List<TransactionDto>> {
        return databaseDao.getAllTransaction()
    }

    override fun eraseTransaction() {
        return databaseDao.eraseTransaction()
    }

    override fun getCurrentDayTransaction(): Flow<List<TransactionDto>> {
        return databaseDao.getCurrentDayExpTransaction()
    }

    override fun getWeeklyTransaction(): Flow<List<TransactionDto>> {
        return databaseDao.getWeeklyExpTransaction()
    }

    override fun getMonthlyTransaction(): Flow<List<TransactionDto>> {
        return databaseDao.getMonthlyExpTransaction()
    }

    override fun getTransactionByType(transactionType: String): Flow<List<TransactionDto>> {
        return databaseDao.getTransactionByType(transactionType)
    }

}