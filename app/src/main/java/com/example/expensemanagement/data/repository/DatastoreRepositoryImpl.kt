package com.example.expensemanagement.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.common.Constants.EXPENSE_TRACKER_KEY
import com.example.expensemanagement.domain.repository.DatastoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = EXPENSE_TRACKER_KEY)
class DatastoreRepositoryImpl @Inject constructor(
    private val context: Context
): DatastoreRepository{
    private val datastore = context.dataStore
    private val onBoardingKey = booleanPreferencesKey(Constants.ONBOARDING_KEY)
    private val selectedCurrency = stringPreferencesKey(Constants.CURRENCY_KEY)
    private val groupKey = booleanPreferencesKey(Constants.GROUP_KEY)
    override suspend fun writeOnboardingKey(completed: Boolean) {
        datastore.edit {store ->
            store[onBoardingKey] = completed
        }
    }

    override suspend fun readOnboardingKey(): Flow<Boolean> {
        return datastore.data.map { preferences ->
            preferences[onBoardingKey] ?: false
        }
    }

    override suspend fun writeCurrency(currency: String) {
        datastore.edit { store ->
            store[selectedCurrency] = currency
        }
    }

    override suspend fun readCurrency(): Flow<String> {
        return datastore.data.map { preferences ->
            preferences[selectedCurrency] ?: String()
        }
    }

    override suspend fun writeGroupKey(completed: Boolean) {
        datastore.edit { store ->
            store[groupKey] = completed
        }
    }

    override suspend fun readGroupKey(): Flow<Boolean> {
        return datastore.data.map { preferences ->
            preferences[groupKey] ?: false
        }
    }

    override suspend fun eraseDatastore() {
        datastore.edit {
            it.remove(onBoardingKey)
            it.remove(selectedCurrency)
            it.remove(groupKey)
        }
    }
}