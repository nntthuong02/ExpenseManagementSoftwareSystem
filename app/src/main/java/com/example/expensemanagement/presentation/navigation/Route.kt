package com.example.expensemanagement.presentation.navigation

sealed class Route(val route: String) {
    object OnboardingScreen: Route("onboarding")
    object WelcomeScreen: Route("welcome")
    object CurrencyScreen: Route("currency")
    object HomeScreen: Route("home")
    object TransactionScreen: Route("transaction")
    object InsightScreen: Route("insight")
    object ParticipantScreen: Route("participant")
    object TransactionDetailScreen: Route("detail")
    object SettingScreen: Route("setting")
    object PayScreen: Route("pay")
}