package com.example.expensemanagement.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensemanagement.data.local.converter.DateConverter
import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.data.local.entity.TransactionDto

@TypeConverters(DateConverter::class)
@Database(entities = [TransactionDto::class, ParticipantDto::class], version = 1, exportSchema = true)
abstract class TransactionDatabase: RoomDatabase(){
    abstract fun transactionDao(): TransactionDao

    abstract fun 

}