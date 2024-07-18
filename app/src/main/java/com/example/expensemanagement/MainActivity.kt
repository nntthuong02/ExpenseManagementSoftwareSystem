@file:OptIn(InternalCoroutinesApi::class, ExperimentalUnitApi::class)

package com.example.expensemanagement

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.domain.usecase.AppEntryUseCase
import com.example.expensemanagement.domain.usecase.GetCurrency
import com.example.expensemanagement.presentation.currency_screen.CurrencyViewModel
import com.example.expensemanagement.presentation.navigation.MainScreen
import com.example.expensemanagement.presentation.navigation.NavGraph
import com.example.expensemanagement.presentation.navigation.Route
import com.example.expensemanagement.presentation.onboarding.OnboardingScreen
import com.example.expensemanagement.ui.theme.ExpenseManagementTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /*Test*/
    //1
    @Inject
    lateinit var appEntryUseCase: AppEntryUseCase
    //2
//    @Inject
//    lateinit var getCurrency: GetCurrency
    //3
    val currencyViewModel by viewModels<CurrencyViewModel>()
    /*Test*/
    @OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        /*Test*/
        lifecycleScope.launch {
            appEntryUseCase.getOnboardingKeyUseCase().collect{
                Log.d("test appEntryUseCase", it.toString())
            }
        }
        Log.d("currency", currencyViewModel.countryCurrencies.value.toString())
//        val currencies = getCurrency.invoke()
//        currencies.forEach {
//            Log.d("CurrencyInfo", it.toString())
//        }
        /*Test*/
        setContent {
            ExpenseManagementTheme {
                Box(
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
                ) {
                    val navController = rememberNavController()
                    MainScreen(startDestination = Route.HomeScreen.route)
                }

            }
        }
    }
}

