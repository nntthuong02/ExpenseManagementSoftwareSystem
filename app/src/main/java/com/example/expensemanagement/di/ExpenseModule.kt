package com.example.expensemanagement.di

import android.content.Context
import androidx.room.Room
import com.example.expensemanagement.data.AppDatabase
import com.example.expensemanagement.data.DatabaseDao
import com.example.expensemanagement.data.repository.DatabaseRepositoryImpl
import com.example.expensemanagement.data.repository.DatastoreRepositoryImpl
import com.example.expensemanagement.domain.repository.DatabaseRepository
import com.example.expensemanagement.domain.repository.DatastoreRepository
import com.example.expensemanagement.domain.usecase.AppEntryUseCase
import com.example.expensemanagement.domain.usecase.read_database.GetAllParticipants
import com.example.expensemanagement.domain.usecase.read_datastore.GetCurrencyUseCase
import com.example.expensemanagement.domain.usecase.read_datastore.GetOnboardingKeyUseCase
import com.example.expensemanagement.domain.usecase.write_datastore.EditCurrencyUseCase
import com.example.expensemanagement.domain.usecase.write_datastore.EditOnboardingKeyUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExpenseModule {

    //error: [Dagger/MissingBinding] android.content.Context cannot be provided without an @Provides-annotated method.
    //Debug: use Application instead of Context ???
    //or: add @ApplicationContext
    @Provides
    @Singleton
    fun provideDatastoreRepository(
        @ApplicationContext context: Context
    ) : DatastoreRepository {
        return DatastoreRepositoryImpl(context)
    }
    //Cung cấp TransactionDao từ TransactionDatabase:
    @Provides
    @Singleton
    fun provideDatabaseDao(database: AppDatabase): DatabaseDao {
        return database.databaseDao()
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(databaseDao: DatabaseDao) : DatabaseRepository{
        return DatabaseRepositoryImpl(databaseDao)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase{
        return Room.databaseBuilder(context, AppDatabase::class.java, "transactionDB")
            .fallbackToDestructiveMigration()
            .build()
    }

    //Test
    @Provides
    @Singleton
    fun provideAppEntryUseCase(
        datastoreRepository: DatastoreRepository,
        databaseRepository: DatabaseRepository
    ) = AppEntryUseCase(
        getOnboardingKeyUseCase = GetOnboardingKeyUseCase(datastoreRepository),
        editOnboardingKeyUseCase = EditOnboardingKeyUseCase(datastoreRepository),
        editCurrencyUseCase = EditCurrencyUseCase(datastoreRepository),
        getCurrencyUseCase = GetCurrencyUseCase(datastoreRepository),
        getAllParticipants = GetAllParticipants(databaseRepository)
    )
}