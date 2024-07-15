package com.example.expensemanagement.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.expensemanagement.domain.models.Fund

@Entity(
    tableName = "fund",
    foreignKeys = [ForeignKey(
        entity = GroupDto::class,
        parentColumns = arrayOf("_id"),
        childColumns = arrayOf("groupId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class FundDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val fundId: Int,
    @ColumnInfo(name = "fundName")
    val fundName: String,
    @ColumnInfo(name = "totalAmount")
    val totalAmount: Double,
    @ColumnInfo(name = "groupId")
    val groupId: Int
){
    fun toFund(): Fund = Fund(fundId, fundName, totalAmount, groupId)
}
