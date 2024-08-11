package com.example.expensemanagement.presentation.navigation

sealed class Route(val route: String) {
    object OnboardingScreen: Route("onboarding")
    object CurrencyScreen: Route("currency")
    object HomeScreen: Route("home")
    object TransactionScreen: Route("transaction")
    object InsightScreen: Route("insight")
    object ParticipantScreen: Route("participant")
    object TransactionDetailScreen: Route("detail")
    object EditTransactionScreen: Route("edit_transaction")
    object PayScreen: Route("pay")
    object ListFundScreen: Route("list_fund")
    object EditFundScreen: Route("edit_fund")
    object ListParticipantScreen: Route("list_participant")
    object EditParticipantScreen: Route("edit_participant")
    object ListTransaction: Route("list_transaction")

    object SettingScreen: Route("setting")
    object Test1: Route("test1")
    object Test2: Route("test2")
    object StatisticsScreen: Route("statistics")
}