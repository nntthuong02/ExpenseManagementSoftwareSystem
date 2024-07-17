package com.example.expensemanagement.presentation.onboarding

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensemanagement.presentation.common.ExButton
import com.example.expensemanagement.presentation.common.ExTextButton
import com.example.expensemanagement.presentation.navigation.NavGraph
import com.example.expensemanagement.presentation.navigation.Route
import com.example.expensemanagement.presentation.onboarding.component.OnboardingPage
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(pageCount = {
            pages.size
        })
        val buttonsState = remember {
            derivedStateOf {
                when (pagerState.currentPage) {
                    0 -> listOf("", "Next")
                    1 -> listOf("Back", "Next")
                    2 -> listOf("Back", "Start")
                    else -> listOf("", "")
                }
            }
        }
        Log.d("test OnboardingScreen", "ok")
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { index ->
            Log.d("test2 OnboardingScreen", "ok")
            OnboardingPage(page = pages[index])
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.padding(start = 8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(16.dp)

                    )
                }
            }

            Row(
                modifier = Modifier.padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                val scope = rememberCoroutineScope()
                if (buttonsState.value[0].isNotEmpty()) {
                    ExTextButton(
                        text = buttonsState.value[0],
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(
                                    page = pagerState.currentPage - 1
                                )
                            }

                        }
                    )
                }

                ExButton(
                    text = buttonsState.value[1],
                    onClick = {
                        scope.launch {
                            if (pagerState.currentPage == 2) {
                                Log.d("onboardingScreen", "navigate")
                                navController.popBackStack()
                                navController.navigate(Route.CurrencyScreen.route)
                            } else {
                                pagerState.animateScrollToPage(
                                    page = pagerState.currentPage + 1
                                )

                            }
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.weight(0.5f))
    }

}