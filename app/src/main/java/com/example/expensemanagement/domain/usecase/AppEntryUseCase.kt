package com.example.expensemanagement.domain.usecase

import com.example.expensemanagement.domain.usecase.read_database.GetAllParticipants
import com.example.expensemanagement.domain.usecase.read_database.GetFundByGroupId
import com.example.expensemanagement.domain.usecase.read_database.GetParticipantByFundId
import com.example.expensemanagement.domain.usecase.read_datastore.GetCurrencyUseCase
import com.example.expensemanagement.domain.usecase.read_datastore.GetOnboardingKeyUseCase
import com.example.expensemanagement.domain.usecase.write_datastore.EditCurrencyUseCase
import com.example.expensemanagement.domain.usecase.write_datastore.EditOnboardingKeyUseCase

data class AppEntryUseCase(
    val getOnboardingKeyUseCase: GetOnboardingKeyUseCase,
    val editOnboardingKeyUseCase: EditOnboardingKeyUseCase,
    val editCurrencyUseCase: EditCurrencyUseCase,
    val getCurrencyUseCase: GetCurrencyUseCase,
    val getAllParticipants: GetAllParticipants,
    val getFundByGroupId: GetFundByGroupId,
    val getParticipantByFundId: GetParticipantByFundId
)
