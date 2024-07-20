package com.example.expensemanagement.presentation.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.presentation.currency_screen.CurrencyScreen
import com.example.expensemanagement.presentation.home.HomeScreen
import com.example.expensemanagement.presentation.onboarding.OnboardingScreen
import com.example.expensemanagement.presentation.transaction_screen.TransactionScreen

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalUnitApi
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Route.OnboardingScreen.route) {
            OnboardingScreen(navController = navController)
        }
        composable(route = Route.CurrencyScreen.route) {
            CurrencyScreen(navController = rememberNavController())
        }
        composable(route = Route.HomeScreen.route) {
            HomeScreen()
        }
        composable(route = Route.TransactionScreen.route) {
            TransactionScreen(navController)
        }
        composable(route = Route.InsightScreen.route) {
            Text("InsightScreen")
        }
        composable(route = Route.InsightScreen.route) {
            Text("InsightScreen")
        }
        composable(route = Route.ParticipantScreen.route) {
            Text("ParticipantScreen")
        }
        composable(
            route = Route.ParticipantDetailScreen.route
        ) {
            Text("ParticipantDetailScreen")
        }
        composable(route = Route.SettingScreen.route) {
            Text("SettingScreen")
        }
        composable(route = Route.PayScreen.route){
            Text("PayScreen")
        }
    }
}