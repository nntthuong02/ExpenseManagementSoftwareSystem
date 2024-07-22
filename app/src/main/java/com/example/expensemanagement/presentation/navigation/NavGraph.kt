package com.example.expensemanagement.presentation.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.expensemanagement.presentation.currency_screen.CurrencyScreen
import com.example.expensemanagement.presentation.home.HomeScreen
import com.example.expensemanagement.presentation.insight_screen.InsightScreen
import com.example.expensemanagement.presentation.insight_screen.ParticipantScreen
import com.example.expensemanagement.presentation.insight_screen.TransactionDetailScreen
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
            CurrencyScreen(navController = navController)
        }
        composable(route = Route.HomeScreen.route) {
            HomeScreen()
        }
        composable(route = Route.TransactionScreen.route) {
            TransactionScreen(navController = navController)
        }
        composable(route = Route.InsightScreen.route) {
            //navController = rememberNavController() -> navController = navController
            //khong dung navController = rememberNavController() khi muon navigate den man hinh khac
            InsightScreen(navController = navController)
        }
//        composable(route = Route.ParticipantScreen.route) {
//            Text("ParticipantScreen")
//        }
        composable(
            route = "${Route.ParticipantScreen.route}/{fundId}",
            arguments = listOf(
                navArgument("fundId"){
                    type = NavType.IntType
                    defaultValue = 1
//                    nullable = true
                }
            )) { backStackEntry  ->
            val fundId = backStackEntry.arguments?.getInt("fundId") ?: 1
            ParticipantScreen(navController = navController, fundId = backStackEntry.arguments!!.getInt("fundId"))
        }

        composable(
            route = "${Route.TransactionDetailScreen.route}/{parId}",
            arguments = listOf(
                navArgument("parId"){
                    type = NavType.IntType
                    defaultValue = 1
//                    nullable = true
                }
            )
        ) {backStackEntry ->
            TransactionDetailScreen(navController = navController, participantId = backStackEntry.arguments?.getInt("parId") ?: 1)
        }
        composable(route = Route.SettingScreen.route) {
            Text("SettingScreen")
        }
        composable(route = Route.PayScreen.route){
            Text("PayScreen")
        }
    }
}