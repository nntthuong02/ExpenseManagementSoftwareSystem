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
import com.example.expensemanagement.presentation.onboarding.OnboardingScreen

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalUnitApi
@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Route.OnboardingScreen.route
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Route.OnboardingScreen.route) {
            OnboardingScreen(navController = navController)
        }
        composable(route = Route.CurrencyScreen.route) {
            Text("CurrencyScreen")
        }
        composable(route = Route.HomeScreen.route) {
            Text("HomeScreen")
        }
        composable(route = Route.TransactionScreen.route) {
            Text("TransactionScreen")
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
            route = "${Route.ParticipantDetailScreen.route}"
        ) {
            Text("ParticipantDetailScreen")
        }
        composable(route = Route.SettingScreen.route) {
            Text("SettingScreen")
        }
    }
}