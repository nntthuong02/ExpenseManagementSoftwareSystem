package com.example.expensemanagement.presentation.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.presentation.home.component.CenterAlignedTopAppBar
import com.example.expensemanagement.presentation.home.component.ParBarChart
import com.example.expensemanagement.presentation.home.component.ScrollContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
//    modifier: Modifier = Modifier
) {
//    val par1 = Participant(1, "Quy2")
//    val par2 = Participant(1, "Quy3")
//    val chartData = listOf(
//        Pair (par1, 1.0),
//        Pair (par1, 300.0),
//        Pair (par1, 1000.0),
//        Pair(par2, 0.0),
//    )
//    Text(text = "SettingScreen")
//    CenterAlignedTopAppBar(
//        name = "Fund",
//        editOnclick = { /*TODO*/ },
//        iconVisible = true,
//        content = {
//            Column(Modifier.padding(paddingValues = it)) {
//                ParBarChart(oxLabel = "Participant", oyLabel = "Money", chartData)
//                Spacer(modifier = Modifier.padding(10.dp))
//                ParBarChart(oxLabel = "Participant", oyLabel = "Money", chartData)
//            }
//
//        },
//        navController = rememberNavController()
//    )

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Column {
                        // Hiển thị Text1 khi cuộn lên

                        // Luôn hiển thị Text chính
                        Text(
                            "Medium Top App Bar",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        AnimatedVisibility(
                            visible = scrollBehavior.state.collapsedFraction == 0f,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Text(
                                "Medium Top App Bar1",
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },

                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        ScrollContent(Modifier.padding(paddingValues = innerPadding))
    }

}