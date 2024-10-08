package com.example.expensemanagement.data.repository

import com.example.expensemanagement.data.DatabaseDao
import com.example.expensemanagement.data.local.entity.FundDto
import com.example.expensemanagement.data.local.entity.GroupDto
import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.data.local.entity.ParticipantFundDto
import com.example.expensemanagement.data.local.entity.TransactionDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val databaseDao: DatabaseDao
): DatabaseRepository{
    //Transaction
    override suspend fun insertTransaction(trans: TransactionDto) {
        databaseDao.insertTransaction(trans)
    }

    override suspend fun markAllAsPaid() {
        databaseDao.markAllAsPaid()
    }

    override fun getTransactionById(trans: Int): Flow<TransactionDto> {
        return databaseDao.getTransactionById(trans)
    }

    override fun getTransactionByDate(entryDate: String): Flow<List<TransactionDto>> {
        return databaseDao.getTransactionByDate(entryDate)
    }

    override fun getTransactionByParticipant(participantId: Int): Flow<List<TransactionDto>> {
        return databaseDao.getTransactionByParticipant(participantId)
    }

    override fun getTransIsNotPaid(): Flow<List<TransactionDto>> {
        return databaseDao.getTransIsNotPaid()
    }

    override fun getTransByFund(fundId: Int): Flow<List<TransactionDto>> {
        return databaseDao.getTransByFund(fundId)
    }

    override fun getTransByFundAndPar(fundId: Int, parId: Int): Flow<List<TransactionDto>> {
        return databaseDao.getTransByFundAndPar(fundId, parId)
    }

    override fun getAllTransactions(): Flow<List<TransactionDto>> {
        return databaseDao.getAllTransaction()
    }

    suspend override fun eraseAllTransaction() {
        return databaseDao.eraseAllTransaction()
    }

    override suspend fun eraseTransactionById(transId: Int) {
        return databaseDao.eraseTransactionById(transId)
    }

    override suspend fun updateTransaction(trans: TransactionDto) {
        return databaseDao.updateTransaction(trans)
    }

    override suspend fun updateTransactionDetails(
        id: Int,
        title: String,
        date: Date,
        amount: Double,
        category: String,
        transactionType: String,
        parId: Int,
        fundId: Int
    ) {
        return databaseDao.updateTransactionDetails(id, title, date, amount, category, transactionType, parId, fundId)
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

    override suspend fun updatePayTransactions(time: String) {
       return databaseDao.updatePayTransactions(time)
    }

    override suspend fun undoPayment(time: String) {
        return databaseDao.undoPayment(time)
    }

    //Group
    override suspend fun insertGroup(group: GroupDto) {
        return databaseDao.insertGroup(group)
    }

    override fun getAllGroups(): Flow<List<GroupDto>> {
        return databaseDao.getAllGroups()
    }

    override fun getGroupById(groupId: Int): Flow<GroupDto> {
        return databaseDao.getGroupById(groupId)
    }

    override suspend fun updateGroup(group: GroupDto) {
        return databaseDao.updateGroup(group)
    }

    override suspend fun eraseGroupById(groupId: Int) {
        return databaseDao.eraseGroupById(groupId)
    }


    //Fund
    override suspend fun insertFund(fund: FundDto): Long {
        return databaseDao.insertFund(fund)
    }

    override fun getAllFunds(): Flow<List<FundDto>> {
        return databaseDao.getAllFunds()
    }

    override fun getFundById(fundId: Int): Flow<FundDto> {
        return databaseDao.getFundById(fundId)
    }

    override fun getFundByGroupId(groupId: Int): Flow<List<FundDto>> {
        return databaseDao.getFundByGroupId(groupId)
    }

    override fun getFundByPar(parId: Int): Flow<List<FundDto>> {
        return databaseDao.getFundByPar(parId)
    }

    override suspend fun updateFund(fund: FundDto) {
        return databaseDao.updateFund(fund)
    }

    override suspend fun eraseFundById(fundId: Int) {
        return databaseDao.eraseFundById(fundId)
    }

    override suspend fun eraseAllFunds() {
        return databaseDao.eraseAllFunds()
    }

    //ParticipantFund
    override suspend fun insertParticipantFund(parFund: ParticipantFundDto) {
        return databaseDao.insertParticipantFund(parFund)
    }

    override fun getAllParticipantFunds(): Flow<List<ParticipantFundDto>> {
        return databaseDao.getAllParticipantFunds()
    }

    override fun getParticipantFundById(parFundId: Int): Flow<ParticipantFundDto> {
        return databaseDao.getParticipantFundById(parFundId)
    }

    override fun getParFundByParAndFund(parId: Int, fundId: Int): Flow<ParticipantFundDto> {
        return databaseDao.getParFundByParAndFund(parId, fundId)
    }


    override suspend fun updateParticipantFund(parFund: ParticipantFundDto) {
        return databaseDao.updateParticipantFund(parFund)
    }

    override suspend fun eraseParFundById(parFundId: Int) {
        return databaseDao.eraseParFundById(parFundId)
    }


    //Participant
    override suspend fun insertParticipant(participant: ParticipantDto) {
        databaseDao.insertParticipant(participant)
    }
    override suspend fun updateParticipant(participant: ParticipantDto) {
        return databaseDao.updateParticipant(participant)
    }

    override suspend fun eraseParticipantById(parId: Int) {
        return databaseDao.eraseParticipantById(parId)
    }

    override suspend fun eraseAllParticipants() {
        return databaseDao.eraseAllParticipants()
    }

    override fun getParticipantByName(participantName: String): Flow<ParticipantDto> {
        return databaseDao.getParticipantByName(participantName)
    }

    override fun getParticipantById(participantId: Int): Flow<ParticipantDto> {
        return databaseDao.getParticipantById(participantId)
    }

    override fun getParticipantByFundId(fundId: Int): Flow<List<ParticipantDto>> {
        return databaseDao.getParticipantByFundId(fundId)
    }

    override fun getAllParticipants(): Flow<List<ParticipantDto>> {
        return databaseDao.getAllParticipants()
    }
}