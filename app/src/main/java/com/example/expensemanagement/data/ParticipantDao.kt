package com.example.expensemanagement.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensemanagement.data.local.entity.ParticipantDto
import kotlinx.coroutines.flow.Flow

@Dao
interface ParticipantDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParticipant(participants: ParticipantDto)

    @Query("SELECT * FROM PARTICIPANT WHERE participant = :participant")
    fun getParticipantByName(participant: String) : Flow<ParticipantDto>

    @Query("SELECT * FROM PARTICIPANT")
    fun getAllParticipants() : Flow<List<ParticipantDto>>
}