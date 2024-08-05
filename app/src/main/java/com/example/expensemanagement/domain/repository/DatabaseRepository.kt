package com.example.expensemanagement.domain.repository

import com.example.expensemanagement.data.local.entity.FundDto
import com.example.expensemanagement.data.local.entity.GroupDto
import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.data.local.entity.ParticipantFundDto
import com.example.expensemanagement.data.local.entity.TransactionDto
import kotlinx.coroutines.flow.Flow
import java.util.Date


interface DatabaseRepository {
    //Transaction
    suspend fun insertTransaction(trans: TransactionDto)

    suspend fun markAllAsPaid()

    fun getTransactionById(trans: Int): Flow<TransactionDto>
    fun getTransactionByDate(entryDate: String): Flow<List<TransactionDto>>

    fun getTransactionByParticipant(participantId: Int): Flow<List<TransactionDto>>

    fun getTransIsNotPaid(): Flow<List<TransactionDto>>

    fun getTransByFund(fundId: Int): Flow<List<TransactionDto>>

    fun getTransByFundAndPar(fundId: Int, parId: Int): Flow<List<TransactionDto>>

    suspend fun eraseAllTransaction()

    suspend fun eraseTransactionById(transId: Int)

    suspend fun updateTransaction(trans: TransactionDto)

    suspend fun updateTransactionDetails(id: Int, title: String, date: Date, amount: Double, category: String, transactionType: String, parId: Int, fundId: Int)

    fun getCurrentDayTransaction(): Flow<List<TransactionDto>>

    fun getWeeklyTransaction(): Flow<List<TransactionDto>>

    fun getMonthlyTransaction(): Flow<List<TransactionDto>>

    fun getTransactionByType(transactionType: String): Flow<List<TransactionDto>>

    suspend fun updatePayTransactions(time: String)

    suspend fun undoPayment(time: String)
    //Group
    suspend fun insertGroup(group: GroupDto)

    fun getAllGroups(): Flow<List<GroupDto>>

    fun getGroupById(groupId: Int): Flow<GroupDto>

    suspend fun updateGroup(group: GroupDto)

    suspend fun eraseGroupById(groupId: Int)
    //Fund

    suspend fun insertFund(fund: FundDto): Long

    fun getAllFunds(): Flow<List<FundDto>>

    fun getFundById(fundId: Int): Flow<FundDto>

    fun getFundByGroupId(groupId: Int): Flow<List<FundDto>>

    fun getFundByPar(parId: Int): Flow<List<FundDto>>

    suspend fun updateFund(fund: FundDto)

    suspend fun eraseFundById(fundId: Int)

    //ParticipantFund
    suspend fun insertParticipantFund(parFund: ParticipantFundDto)

    fun getAllParticipantFunds(): Flow<List<ParticipantFundDto>>

    fun getParticipantFundById(parFundId: Int): Flow<ParticipantFundDto>

    fun getParFundByParAndFund(parId: Int, fundId: Int): Flow<ParticipantFundDto>

    suspend fun updateParticipantFund(parFund: ParticipantFundDto)

    suspend fun eraseParFundById(parFundId: Int)


    //Participant
    suspend fun insertParticipant(participant: ParticipantDto)
    fun getParticipantByName(participantName: String): Flow<ParticipantDto>

    fun getParticipantById(participantId: Int): Flow<ParticipantDto>

    fun getParticipantByFundId(fundId: Int): Flow<List<ParticipantDto>>

    fun getAllParticipants(): Flow<List<ParticipantDto>>

    fun getAllTransactions(): Flow<List<TransactionDto>>

    suspend fun updateParticipant(participant: ParticipantDto)
    suspend fun eraseParticipantById(parId: Int)
}