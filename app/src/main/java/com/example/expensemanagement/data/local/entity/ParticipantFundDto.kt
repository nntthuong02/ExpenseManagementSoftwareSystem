package com.example.expensemanagement.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.expensemanagement.domain.models.ParticipantFund

@Entity(
    tableName = "ParticipantFund_table",
    foreignKeys = [
        ForeignKey(
            entity = ParticipantDto::class,
            parentColumns = arrayOf("_id"),
            childColumns = arrayOf("participantId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = FundDto::class,
            parentColumns = arrayOf("_id"),
            childColumns = arrayOf("fundId"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["participantId"],
//            unique = true
        ),
        Index(
            value = ["fundId"],
//            unique = true
        )],
)
data class ParticipantFundDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val parFundId: Int,
    @ColumnInfo(name = "participantId")
    val participantId: Int,
    @ColumnInfo(name = "fundId")
    val fundId: Int
){
    fun toParticipantFund(): ParticipantFund = ParticipantFund(parFundId, participantId, fundId)
}
