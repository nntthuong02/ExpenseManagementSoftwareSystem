package com.example.expensemanagement.presentation.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.expensemanagement.presentation.currency_screen.CurrencyScreen
import com.example.expensemanagement.presentation.home.EditFundScreen
import com.example.expensemanagement.presentation.home.EditParticipantScreen
import com.example.expensemanagement.presentation.home.HomeScreen
import com.example.expensemanagement.presentation.home.ListFundScreen
import com.example.expensemanagement.presentation.home.ListParticipantScreen
import com.example.expensemanagement.presentation.home.ListTransactionScreen
import com.example.expensemanagement.presentation.home.StatisticsScreen
import com.example.expensemanagement.presentation.insight_screen.EditTransactionScreen
import com.example.expensemanagement.presentation.insight_screen.InsightScreen
import com.example.expensemanagement.presentation.insight_screen.ParticipantScreen
import com.example.expensemanagement.presentation.insight_screen.TransactionDetailScreen
import com.example.expensemanagement.presentation.onboarding.OnboardingScreen
import com.example.expensemanagement.presentation.payment_screen.PaymentScreen
import com.example.expensemanagement.presentation.setting.SettingScreen
import com.example.expensemanagement.presentation.setting.Test2
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
            HomeScreen(navController = navController)
        }
        composable(route = Route.ListFundScreen.route){
            ListFundScreen(navController = navController)
        }
        composable(
            route = "${Route.EditFundScreen.route}/{fundId}",
            arguments = listOf(
                navArgument("fundId"){
                    type = NavType.IntType
                    defaultValue = 1
//                    nullable = true
                }
            )
            ){ backStackEntry  ->
            val fundId = backStackEntry.arguments!!.getInt("fundId")
            EditFundScreen(fundId = fundId, navController = navController)
        }
        composable(route = Route.ListParticipantScreen.route){
            ListParticipantScreen(navController = navController)
        }
        composable(
            route = "${Route.EditParticipantScreen.route}/{parId}",
            arguments = listOf(
                navArgument("parId"){
                    type = NavType.IntType
                    defaultValue = 1
//                    nullable = true
                }
            )
        ){backStackEntry ->
            val parId = backStackEntry.arguments!!.getInt("parId")
            EditParticipantScreen(parId = parId, navController = navController)
        }
        composable(route = Route.ListTransaction.route){
            ListTransactionScreen(navController)
        }
        composable(route = Route.StatisticsScreen.route){
            StatisticsScreen()
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
            val fundId = backStackEntry.arguments!!.getInt("fundId")
            ParticipantScreen(navController = navController, fundId = fundId)
        }

        composable(
            route = "${Route.TransactionDetailScreen.route}/{parId}?fundId={fundId}",
            arguments = listOf(
                navArgument("parId"){
                    type = NavType.IntType
                    defaultValue = 1
//                    nullable = true
                },
                navArgument("fundId"){
                    type = NavType.IntType
                    defaultValue = 1
//                    nullable = true
                }
            )
        ) {backStackEntry ->
            TransactionDetailScreen(navController = navController, participantId = backStackEntry.arguments?.getInt("parId") ?: 1, fundId = backStackEntry.arguments?.getInt("fundId") ?: 1)
        }
        composable(
            route = "${Route.EditTransactionScreen.route}/{transId}?fundId={fundId}",
            arguments = listOf(
                navArgument("transId"){
                    type = NavType.IntType
                    defaultValue = 1
                },
                navArgument("fundId"){
                    type = NavType.IntType
                    defaultValue = 1
//                    nullable = true
                }
            )
        ) {backStackEntry ->
            EditTransactionScreen(transactionId = backStackEntry.arguments?.getInt("transId") ?: 1, fundId = backStackEntry.arguments?.getInt("fundId") ?: 1, navController = navController)
        }

        composable(route = Route.PayScreen.route){
            PaymentScreen(navController = navController)
        }
        composable(route = Route.SettingScreen.route){
            SettingScreen()
        }

    }
}