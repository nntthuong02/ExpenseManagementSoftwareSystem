package com.example.expensemanagement.domain.usecase.write_database

import com.example.expensemanagement.data.local.entity.GroupDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import javax.inject.Inject

class InsertNewGroup @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(group: GroupDto) {
        databaseRepository.insertGroup(group)
    }
}