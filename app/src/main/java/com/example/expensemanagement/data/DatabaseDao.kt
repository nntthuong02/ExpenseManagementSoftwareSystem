package com.example.expensemanagement.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensemanagement.common.TransactionType
import com.example.expensemanagement.data.local.entity.ParticipantDto
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

    @Query("SELECT * " +
            "FROM TRANSACTION_TABLE AS T " +
            "INNER JOIN PARTICIPANT AS P ON P._id = T.participantId " +
            "INNER JOIN PARTICIPANTFUND AS PF ON P._ID = PF.PARTICIPANTID " +
            "WHERE PF._id = :fundId")
    fun getTransByFund(fundId: Int): Flow<List<TransactionDto>>
    @Query("SELECT * " +
            "FROM TRANSACTION_TABLE AS T " +
            "INNER JOIN PARTICIPANT AS P ON P._id = T.participantId " +
            "INNER JOIN PARTICIPANTFUND AS PF ON P._ID = PF.PARTICIPANTID " +
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

    @Query("SELECT * FROM PARTICIPANT WHERE participant = :participant")
    fun getParticipantByName(participant: String) : Flow<ParticipantDto>

    @Query("SELECT * FROM PARTICIPANT")
    fun getAllParticipants() : Flow<List<ParticipantDto>>
}
