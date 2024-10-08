package com.example.expensemanagement.data

import android.content.Context
import android.content.Intent
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.expensemanagement.data.local.converter.DateConverter
import com.example.expensemanagement.data.local.entity.FundDto
import com.example.expensemanagement.data.local.entity.GroupDto
import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.data.local.entity.ParticipantFundDto
import com.example.expensemanagement.data.local.entity.TransactionDto
import java.io.File
import java.io.IOException

@TypeConverters(DateConverter::class)
@Database(
    entities = [TransactionDto::class, ParticipantDto::class, GroupDto::class, FundDto::class, ParticipantFundDto::class],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase(){
    abstract fun databaseDao(): DatabaseDao


}