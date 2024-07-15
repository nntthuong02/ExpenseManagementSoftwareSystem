package com.example.expensemanagement.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.expensemanagement.domain.models.Transaction
import java.util.Date

@Entity(
    tableName = "transaction",
    foreignKeys = [ForeignKey(
        entity = ParticipantDto::class,
        parentColumns = arrayOf("_id"),
        childColumns = arrayOf("participantId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class TransactionDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val transactionId: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "timestamp")
    val date: Date,
    @ColumnInfo(name = "dateOfEntry")
    val dateOfEntry: String,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "isPaid")
    val isPaid: Boolean,
    @ColumnInfo(name = "transactionType")
    val transactionType: String,
    @ColumnInfo(name = "participantId")
    val participantId: Int,
){
    fun toTransaction(): Transaction = Transaction(transactionId, title, date, dateOfEntry, amount, category, isPaid, transactionType, participantId)
}
