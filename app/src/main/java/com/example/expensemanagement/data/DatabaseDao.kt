package com.example.expensemanagement.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Update
import com.example.expensemanagement.common.TransactionType
import com.example.expensemanagement.data.local.entity.FundDto
import com.example.expensemanagement.data.local.entity.GroupDto
import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.data.local.entity.ParticipantFundDto
import com.example.expensemanagement.data.local.entity.TransactionDto
import kotlinx.coroutines.flow.Flow
import java.util.Date


@Dao
interface DatabaseDao {
    //Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionDto)

    @Query("UPDATE TRANSACTION_TABLE SET isPaid = 1")
    suspend fun markAllAsPaid()

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE dateOfEntry = :entryDate")
    fun getTransactionByDate(entryDate: String) : Flow<List<TransactionDto>>

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE _id = :trans")
    fun getTransactionById(trans: Int): Flow<TransactionDto>

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE participantId = :parId")
    fun getTransactionByParticipant(parId: Int): Flow<List<TransactionDto>>

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE isPaid = 0")
    fun getTransIsNotPaid(): Flow<List<TransactionDto>>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM TRANSACTION_TABLE WHERE fundId = :fundId")
    fun getTransByFund(fundId: Int): Flow<List<TransactionDto>>
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM TRANSACTION_TABLE WHERE fundId = :fundId AND participantId = :parId")
    fun getTransByFundAndPar(fundId: Int, parId: Int): Flow<List<TransactionDto>>

    @Query("SELECT * FROM TRANSACTION_TABLE")
    fun getAllTransaction() : Flow<List<TransactionDto>>

    @Query("DELETE FROM TRANSACTION_TABLE")
    suspend fun eraseAllTransaction()

    @Query("DELETE FROM TRANSACTION_TABLE WHERE _id = :transId")
    suspend fun eraseTransactionById(transId: Int)

    @Update
    suspend fun updateTransaction(trans: TransactionDto)

    @Query("UPDATE transaction_table SET title = :title, timestamp = :date, amount = :amount, category = :category, transactionType = :transactionType, participantId = :parId, fundId = :fundId WHERE _id = :id")
    suspend fun updateTransactionDetails(id: Int, title: String, date: Date, amount: Double, category: String, transactionType: String, parId: Int, fundId: Int)

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE dateOfEntry = date('now', 'localtime') AND transactionType = :transactionType")
    fun getCurrentDayExpTransaction(transactionType: String = TransactionType.EXPENSE.title): Flow<List<TransactionDto>>

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE dateOfEntry BETWEEN date('now', '-7 day') AND date('now', 'localtime') AND transactionType = :transactionType")
    fun getWeeklyExpTransaction(transactionType: String = TransactionType.EXPENSE.title): Flow<List<TransactionDto>>

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE dateOfEntry BETWEEN date('now', '-1 month') AND date('now', 'localtime') AND transactionType = :transactionType")
    fun getMonthlyExpTransaction(transactionType: String = TransactionType.EXPENSE.title): Flow<List<TransactionDto>>

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE transactionType = :transactionType")
    fun getTransactionByType(transactionType: String): Flow<List<TransactionDto>>

    @Query("UPDATE transaction_table SET dateOfEntry = :time, isPaid = 1 WHERE isPaid = 0")
    suspend fun updatePayTransactions(time: String)

    @Query("UPDATE TRANSACTION_TABLE SET dateOfEntry = '', isPaid = 0 WHERE dateOfEntry = :time" )
    suspend fun undoPayment(time: String)
    //Group
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupDto)

    @Query("SELECT * FROM GROUP_TABLE")
    fun getAllGroups(): Flow<List<GroupDto>>

    @Query("SELECT * FROM GROUP_TABLE WHERE _ID = :groupId")
    fun getGroupById(groupId: Int): Flow<GroupDto>

    @Update
    suspend fun updateGroup(group: GroupDto)

    @Query("DELETE FROM GROUP_TABLE WHERE _id = :groupId")
    suspend fun eraseGroupById(groupId: Int)

    //Fund
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFund(fund: FundDto): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFundGetId(fund: FundDto): Long

    @Query("SELECT * FROM FUND_TABLE")
    fun getAllFunds(): Flow<List<FundDto>>

    @Query("SELECT * FROM FUND_TABLE WHERE _ID = :fundId")
    fun getFundById(fundId: Int): Flow<FundDto>

    @Query("SELECT * FROM FUND_TABLE WHERE FUND_TABLE.groupId = :groupId ")
    fun getFundByGroupId(groupId: Int): Flow<List<FundDto>>

    @Query("SELECT F._id, F.fundName, F.groupId " +
            "FROM FUND_TABLE AS F " +
            "INNER JOIN PARTICIPANTFUND_TABLE AS PF ON PF.fundId = F._ID " +
            "WHERE PF.participantId = :parId")
    fun getFundByPar(parId: Int): Flow<List<FundDto>>

    @Update
    suspend fun updateFund(fund: FundDto)

    @Query("DELETE FROM FUND_TABLE WHERE _id = :fundId")
    suspend fun eraseFundById(fundId: Int)

    @Query("DELETE FROM FUND_TABLE WHERE _id != 1")
    suspend fun eraseAllFunds()

    //ParticipantFund
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipantFund(parFund: ParticipantFundDto)

    @Query("SELECT * FROM PARTICIPANTFUND_TABLE")
    fun getAllParticipantFunds(): Flow<List<ParticipantFundDto>>

    @Query("SELECT * FROM PARTICIPANTFUND_TABLE WHERE _ID = :parFundId")
    fun getParticipantFundById(parFundId: Int): Flow<ParticipantFundDto>

    @Query("SELECT * FROM PARTICIPANTFUND_TABLE WHERE participantId = :parId AND fundId = :fundId")
    fun getParFundByParAndFund(parId: Int, fundId: Int): Flow<ParticipantFundDto>

    @Update
    suspend fun updateParticipantFund(parFund: ParticipantFundDto)

    @Query("DELETE FROM PARTICIPANTFUND_TABLE WHERE _id = :parFundId")
    suspend fun eraseParFundById(parFundId: Int)

    //Participant
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipant(participants: ParticipantDto)

    @Query("SELECT * FROM PARTICIPANT_TABLE WHERE participantName = :participant")
    fun getParticipantByName(participant: String) : Flow<ParticipantDto>

    @Query("SELECT * FROM PARTICIPANT_TABLE WHERE _id = :participantId")
    fun getParticipantById(participantId: Int): Flow<ParticipantDto>

    @Query("SELECT * FROM PARTICIPANT_TABLE")
    fun getAllParticipants() : Flow<List<ParticipantDto>>

    @Query("SELECT P._id, P.participantName " +
            "FROM PARTICIPANT_TABLE AS P " +
            "INNER JOIN PARTICIPANTFUND_TABLE AS PF ON PF.PARTICIPANTID = P._ID " +
            "WHERE PF.fundId = :fundId")
    fun getParticipantByFundId(fundId: Int): Flow<List<ParticipantDto>>

    @Update
    suspend fun updateParticipant(participant: ParticipantDto)

    @Query("DELETE FROM PARTICIPANT_TABLE WHERE _id = :transactionId")
    suspend fun eraseParticipantById(transactionId: Int)

    @Query("DELETE FROM PARTICIPANT_TABLE WHERE _id != 1")
    suspend fun eraseAllParticipants()
}
