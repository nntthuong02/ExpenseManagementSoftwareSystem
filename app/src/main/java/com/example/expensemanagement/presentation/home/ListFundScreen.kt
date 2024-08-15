package com.example.expensemanagement.presentation.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.R
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.presentation.home.component.AddEntity
import com.example.expensemanagement.presentation.home.component.CenterAlignedTopAppBar
import com.example.expensemanagement.presentation.home.component.DetailEntityItem
import com.example.expensemanagement.presentation.home.component.DialogName
import com.example.expensemanagement.presentation.home.component.EntityItem
import com.example.expensemanagement.presentation.insight_screen.component.AlertDialogComponent
import com.example.expensemanagement.presentation.navigation.Route
import com.example.expensemanagement.ui.theme.Blue1
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListFundScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val fundAndExpense by homeViewModel.fundAndExpense.collectAsState()
    val numberTransOfFund by homeViewModel.numberTransOfFund.collectAsState()
    val showSnack = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val openAlertDialog = remember { mutableStateOf(false) }
    val openDeleteDialog = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val fundTitle by remember { mutableStateOf(homeViewModel.fundName) }
    var fundNameFieldValue = TextFieldValue(fundTitle.collectAsState().value)

    LaunchedEffect(Unit) {
        launch { homeViewModel.getFundByGroup() }
        launch { homeViewModel.fetchFundAndExpense() }
        launch { homeViewModel.getNumberTransOfFund() }
    }

    CenterAlignedTopAppBar(
        showSnackbarText = "Please enter name",
        name = "Fund",
        rightIcon1 = R.drawable.add_24px,
        rightIcon2 = R.drawable.delete_24px,
        iconOnclick1 = { openAlertDialog.value = true },
        iconOnlick2 = { openDeleteDialog.value = true},
        showIconRight1 = true,
        showIconRight2 = true,
        showIconLeft = true,
        showSnackbar = showSnack,
        navController = navController
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(openAlertDialog.value == true){
                DialogName(
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = {
                        coroutineScope.launch {
                        if (fundNameFieldValue.text.isEmpty()) {
                            openAlertDialog.value = false
                           showSnack.value = true
//                                snackbarHostState.showSnackbar("Please enter name")

                        } else {
                            openAlertDialog.value = false
                            homeViewModel.insertFund(fundTitle.value, 1)
//
                            navController.navigate("${Route.ListFundScreen.route}")
                            Toast.makeText(context, "Additional funds successfully", Toast.LENGTH_SHORT).show()
                        }
                    }
                                     },
                    name = fundNameFieldValue.text,
                    onNameChange = {text ->
                                   homeViewModel.setFundName(text)
                    },
                    dialogTitle = "Enter the name of the fund you want to create!",
                    dialogText = "fund",
                    iconId = R.drawable.post_add_24px
                )
            }
            if (openDeleteDialog.value){
                AlertDialogComponent(
                    onDismissRequest = { openDeleteDialog.value = false},
                    onConfirmation = {
                        openDeleteDialog.value = false
                        coroutineScope.launch {
                            homeViewModel.eraseAllFund()
                        }
                        navController.navigate(Route.ListFundScreen.route)
                        Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show()
                                     },
                    dialogTitle = "Confirm deletion!",
                    dialogText = "This action will delete all funds except the default fund!",
                    icon = Icons.Filled.Delete
                )
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {


                LazyColumn {
                    itemsIndexed(fundAndExpense) { _, (fund, expense) ->

                        var numberTrans = 0
                        numberTransOfFund.forEach { (fund2, number) ->
                            if(fund2.fundId == fund.fundId ){numberTrans = number}
                        }
                        DetailEntityItem(
                            name = fund.fundName,
                            numberTransaction = numberTrans,
                            amount = homeViewModel.formatAmount(expense),
                            itemOnClick = {

                                navController.navigate("${Route.EditFundScreen.route}/${fund!!.fundId}")

                                          },
                            backgroundColor = Color.DarkGray.copy(alpha = 0.1f),
                            amountType = "EXPENSE: ",
                            surfaceColor = Blue1
                        )

                    }
                }
                SnackbarHost(hostState = snackbarHostState)
            }



        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun ListFundPreview() {
    ListFundScreen(navController = rememberNavController())
}