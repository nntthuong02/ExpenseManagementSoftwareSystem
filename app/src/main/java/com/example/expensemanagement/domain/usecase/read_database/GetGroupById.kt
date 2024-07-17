package com.example.expensemanagement.domain.usecase.read_database

import com.example.expensemanagement.data.local.entity.GroupDto
import com.example.expensemanagement.data.local.entity.TransactionDto
import com.example.expensemanagement.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGroupById @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    operator fun invoke(groupId: Int): Flow<GroupDto>
    {
        return databaseRepository.getGroupById(groupId)
    }
}