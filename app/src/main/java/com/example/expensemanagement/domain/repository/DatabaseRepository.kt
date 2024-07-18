package com.example.expensemanagement.domain.repository

import com.example.expensemanagement.data.local.entity.FundDto
import com.example.expensemanagement.data.local.entity.GroupDto
import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.data.local.entity.ParticipantFundDto
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

    fun getParticipantByFundId(fundId: Int): Flow<List<ParticipantDto>>

    fun getAllParticipants(): Flow<List<ParticipantDto>>

    fun getAllTransactions(): Flow<List<TransactionDto>>

    fun eraseTransaction()

    fun getCurrentDayTransaction(): Flow<List<TransactionDto>>

    fun getWeeklyTransaction(): Flow<List<TransactionDto>>

    fun getMonthlyTransaction(): Flow<List<TransactionDto>>

    fun getTransactionByType(transactionType: String): Flow<List<TransactionDto>>

    suspend fun insertGroup(group: GroupDto)

    fun getAllGroups(): Flow<List<GroupDto>>

    fun getGroupById(groupId: Int): Flow<GroupDto>

    suspend fun insertFund(fund: FundDto)

    fun getAllFunds(): Flow<List<FundDto>>

    fun getFundById(fundId: Int): Flow<FundDto>

    fun getFundByGroupId(groupId: Int): Flow<List<FundDto>>

    suspend fun insertParticipantFund(parFund: ParticipantFundDto)

    fun getAllParticipantFunds(): Flow<List<ParticipantFundDto>>

    fun getParticipantFundById(parFundId: Int): Flow<ParticipantFundDto>
}