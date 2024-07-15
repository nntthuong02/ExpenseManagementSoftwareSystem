package com.example.expensemanagement.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.expensemanagement.domain.models.Group

@Entity(tableName = "group")
data class GroupDto(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val groupId: Int,
    @ColumnInfo(name = "groupName")
    val groupName: String
){
    fun toGroup(): Group = Group(groupId, groupName)
}
