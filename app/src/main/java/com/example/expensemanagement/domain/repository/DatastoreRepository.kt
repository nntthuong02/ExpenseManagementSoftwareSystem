package com.example.expensemanagement.domain.repository

import kotlinx.coroutines.flow.Flow

interface DatastoreRepository {
    suspend fun writeOnboardingKey(completed: Boolean)
    suspend fun readOnboardingKey(): Flow<Boolean>
    suspend fun writeCurrency(currency: String)
    suspend fun readCurrency(): Flow<String>
    suspend fun writeGroupKey(completed: Boolean)
    suspend fun readGroupKey(): Flow<Boolean>
    suspend fun eraseDatastore()
}