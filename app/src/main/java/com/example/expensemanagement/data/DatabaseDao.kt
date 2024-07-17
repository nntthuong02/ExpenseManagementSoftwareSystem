package com.example.expensemanagement.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import com.example.expensemanagement.common.TransactionType
import com.example.expensemanagement.data.local.entity.FundDto
import com.example.expensemanagement.data.local.entity.GroupDto
import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.data.local.entity.ParticipantFundDto
import com.example.expensemanagement.data.local.entity.TransactionDto
import kotlinx.coroutines.flow.Flow


@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionDto)

    @Query("UPDATE TRANSACTION_TABLE SET isPaid = 1")
    suspend fun markAllAsPaid()

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE dateOfEntry = :entryDate")
    fun getTransactionByDate(entryDate: String) : Flow<List<TransactionDto>>

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE participantId = :parId")
    fun getTransactionByParticipant(parId: Int): Flow<List<TransactionDto>>

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE _id = :transId")
    fun getTransIsNotPaidById(transId: Int): Flow<List<TransactionDto>>

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * " +
            "FROM TRANSACTION_TABLE AS T " +
            "INNER JOIN PARTICIPANT_TABLE AS P ON P._id = T.participantId " +
            "INNER JOIN PARTICIPANTFUND_TABLE AS PF ON P._ID = PF.PARTICIPANTID " +
            "WHERE PF._id = :fundId")
    fun getTransByFund(fundId: Int): Flow<List<TransactionDto>>
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * " +
            "FROM TRANSACTION_TABLE AS T " +
            "INNER JOIN PARTICIPANT_TABLE AS P ON P._id = T.participantId " +
            "INNER JOIN PARTICIPANTFUND_TABLE AS PF ON P._ID = PF.PARTICIPANTID " +
            "WHERE PF._id = :fundId AND P._id = :parId")
    fun getTransByFundAndPar(fundId: Int, parId: Int): Flow<List<TransactionDto>>

    @Query("SELECT * FROM TRANSACTION_TABLE")
    fun getAllTransaction() : Flow<List<TransactionDto>>

    @Query("DELETE FROM TRANSACTION_TABLE")
    fun eraseTransaction()

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE dateOfEntry = date('now', 'localtime') AND transactionType = :transactionType")
    fun getCurrentDayExpTransaction(transactionType: String = TransactionType.EXPENSE.title): Flow<List<TransactionDto>>

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE dateOfEntry BETWEEN date('now', '-7 day') AND date('now', 'localtime') AND transactionType = :transactionType")
    fun getWeeklyExpTransaction(transactionType: String = TransactionType.EXPENSE.title): Flow<List<TransactionDto>>

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE dateOfEntry BETWEEN date('now', '-1 month') AND date('now', 'localtime') AND transactionType = :transactionType")
    fun getMonthlyExpTransaction(transactionType: String = TransactionType.EXPENSE.title): Flow<List<TransactionDto>>

    @Query("SELECT * FROM TRANSACTION_TABLE WHERE transactionType = :transactionType")
    fun getTransactionByType(transactionType: String): Flow<List<TransactionDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipant(participants: ParticipantDto)

    @Query("SELECT * FROM PARTICIPANT_TABLE WHERE participantName = :participant")
    fun getParticipantByName(participant: String) : Flow<ParticipantDto>

    @Query("SELECT * FROM PARTICIPANT_TABLE")
    fun getAllParticipants() : Flow<List<ParticipantDto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: GroupDto)

    @Query("SELECT * FROM GROUP_TABLE")
    fun getAllGroups(): Flow<List<GroupDto>>

    @Query("SELECT * FROM GROUP_TABLE WHERE _ID = :groupId")
    fun getGroupById(groupId: Int): Flow<GroupDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFund(fund: FundDto)

    @Query("SELECT * FROM FUND_TABLE")
    fun getAllFunds(): Flow<List<FundDto>>

    @Query("SELECT * FROM FUND_TABLE WHERE _ID = :fundId")
    fun getFundById(fundId: Int): Flow<FundDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipantFund(parFund: ParticipantFundDto)

    @Query("SELECT * FROM PARTICIPANTFUND_TABLE")
    fun getAllParticipantFunds(): Flow<List<ParticipantFundDto>>

    @Query("SELECT * FROM PARTICIPANTFUND_TABLE WHERE _ID = :parFundId")
    fun getParticipantFundById(parFundId: Int): Flow<ParticipantFundDto>
}
