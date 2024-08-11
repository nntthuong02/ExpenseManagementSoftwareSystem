package com.example.expensemanagement.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.presentation.navigation.component.BottomNavBar
import com.example.expensemanagement.presentation.navigation.component.provideBottomNavItems

@ExperimentalAnimationApi
@ExperimentalUnitApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun MainScreen(
    startDestination: String
) {
    val navController = rememberNavController()
    val initialDestinations = listOf(
        Route.OnboardingScreen.route,
        Route.CurrencyScreen.route
    )
    val bottomNavBarState = remember { mutableStateOf(false) }
    val navBarEntry by navController.currentBackStackEntryAsState()
    //Kiem tra co phai dich den bat dau
    bottomNavBarState.value = !initialDestinations.contains(navBarEntry?.destination?.route)
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                //an hien thanh dieu huong
                visible = bottomNavBarState.value,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
            BottomNavBar(
                items = provideBottomNavItems(), navController
            )
            //itemClick
            {
                navController.popBackStack()
                navController.navigate(it.route) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                    restoreState = true
                }

            }
        }
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(
                bottom = it.calculateBottomPadding()
            )
        ) {
            NavGraph(
                navController = navController,
                startDestination = startDestination
            )
        }
    }
}


@ExperimentalAnimationApi
@ExperimentalUnitApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen(startDestination = Route.HomeScreen.route)
}