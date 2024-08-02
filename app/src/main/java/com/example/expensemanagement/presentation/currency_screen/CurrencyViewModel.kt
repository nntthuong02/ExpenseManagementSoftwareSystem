package com.example.expensemanagement.presentation.currency_screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanagement.data.local.entity.FundDto
import com.example.expensemanagement.data.local.entity.GroupDto
import com.example.expensemanagement.data.local.entity.ParticipantDto
import com.example.expensemanagement.data.local.entity.ParticipantFundDto
import com.example.expensemanagement.domain.models.CurrencyModel
import com.example.expensemanagement.domain.usecase.GetCurrency
import com.example.expensemanagement.domain.usecase.write_database.InsertNewFund
import com.example.expensemanagement.domain.usecase.write_database.InsertNewGroup
import com.example.expensemanagement.domain.usecase.write_database.InsertNewParticipant
import com.example.expensemanagement.domain.usecase.write_database.InsertNewParticipantFund
import com.example.expensemanagement.domain.usecase.write_datastore.EditCurrencyUseCase
import com.example.expensemanagement.domain.usecase.write_datastore.EditOnboardingKeyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val editOnboardingUseCase: EditOnboardingKeyUseCase,
    private val editCurrencyUseCase: EditCurrencyUseCase,
    private val insertNewParticipant: InsertNewParticipant,
    private val insertNewGroup: InsertNewGroup,
    private val insertNewFund: InsertNewFund,
    private val insertNewParticipantFund: InsertNewParticipantFund,
    getCurrency: GetCurrency
): ViewModel(){
    var countryCurrencies = mutableStateOf(emptyMap<Char, List<CurrencyModel>>())
        private set

    init {
        countryCurrencies.value = getCurrency().groupBy { it.country[0] }
        Log.d("test currency", countryCurrencies.value.toString())
    }

    fun saveOnboardingState(completed: Boolean) {
        viewModelScope.launch(IO) {
            editOnboardingUseCase(completed = completed)
        }
    }
    fun saveCurrency(currency: String) {
        viewModelScope.launch(IO) {
            editCurrencyUseCase(currency)
        }
    }

    fun createEntity() {
        viewModelScope.launch(IO) {
            insertNewGroup.invoke(GroupDto(1, "My Group"))
            insertNewFund.invoke(FundDto(1, "My Fund",  1))
            insertNewParticipant.invoke((ParticipantDto(1, "I")))
            insertNewParticipantFund.invoke(ParticipantFundDto(1, 1,1))
        }
    }

}