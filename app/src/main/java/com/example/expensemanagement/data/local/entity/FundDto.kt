package com.example.expensemanagement.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.expensemanagement.domain.models.Fund

@Entity(
    tableName = "fund_table",
    foreignKeys = [ForeignKey(
        entity = GroupDto::class,
        parentColumns = arrayOf("_id"),
        childColumns = arrayOf("groupId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["groupId"],
//            unique = true
        )]
)
data class FundDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val fundId: Int,
    @ColumnInfo(name = "fundName")
    val fundName: String,
    @ColumnInfo(name = "groupId")
    val groupId: Int
){
    fun toFund(): Fund = Fund(fundId, fundName, groupId)
}
