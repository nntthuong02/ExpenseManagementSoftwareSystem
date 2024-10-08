package com.example.expensemanagement.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensemanagement.domain.models.Participant

@Entity(tableName = "participant_table")
data class ParticipantDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val participantId: Int,
    @ColumnInfo(name = "participantName")
    val participantName: String,
){
    fun toParticipant(): Participant = Participant(participantId, participantName)
}
