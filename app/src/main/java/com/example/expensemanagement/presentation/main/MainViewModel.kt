package com.example.expensemanagement.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensemanagement.domain.usecase.read_datastore.GetOnboardingKeyUseCase
import com.example.expensemanagement.presentation.navigation.Route
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@InternalCoroutinesApi
class MainViewModel @Inject constructor(
    private val getOnBoardingKeyUseCase: GetOnboardingKeyUseCase
) : ViewModel() {
    var startDestination = MutableStateFlow(Route.OnboardingScreen.route)
        private set

    init {
        viewModelScope.launch(IO) {
            getOnBoardingKeyUseCase().collect { completed ->
                if (completed)
                    startDestination.value = Route.HomeScreen.route
                else startDestination.value = Route.OnboardingScreen.route
            }
        }
    }
}