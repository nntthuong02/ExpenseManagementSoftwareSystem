package com.example.expensemanagement.presentation.home

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
import com.example.expensemanagement.presentation.home.component.DialogAddName
import com.example.expensemanagement.presentation.home.component.EntityItem
import com.example.expensemanagement.presentation.navigation.Route
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListFundScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val fundByGroupId by homeViewModel.fundByGroupId.collectAsState()
    val transByFund by homeViewModel.transByFund.collectAsState()
    val expense by homeViewModel.expense.collectAsState()
    val fundAndExpense by homeViewModel.fundAndExpense.collectAsState()
    val numberTransOfFund by homeViewModel.numberTransOfFund.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val openAlertDialog = remember { mutableStateOf(false) }
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
        name = "Fund",
        rightIcon = R.drawable.add_24px,
        editOnclick = { openAlertDialog.value = true },
        showIconRight = true,
        showIconLeft = true,
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
                DialogAddName(
                    onDismissRequest = { openAlertDialog.value = false },
                    onConfirmation = {
                        coroutineScope.launch {
                        if (fundNameFieldValue.text.isEmpty()) {
                            // Hiển thị Snackbar thông báo lỗi
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please enter name")
                            }
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
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth(),
//                // Chiếm 10% không gian
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//
//                AddEntity(nameEntity = "Fund", name = fundNameFieldValue.text, onNameChange = {
//                    homeViewModel.setFundName(it)
//                }) {
//                    coroutineScope.launch {
//                        if (fundNameFieldValue.text.isEmpty()) {
//                            // Hiển thị Snackbar thông báo lỗi
//                            coroutineScope.launch {
//                                snackbarHostState.showSnackbar("Please enter name")
//                            }
//                        } else {
//                            homeViewModel.insertFund(fundTitle.value, 1)
//                            navController.navigateUp()
//                            navController.navigate("${Route.ListFundScreen.route}")
//                            Toast.makeText(context, "Add successfully", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//                Box(
//                    modifier = Modifier
//                        .background(Color.Green) // Màu nền cho văn bản
//                        .padding(0.dp) // Khoảng cách giữa văn bản và viền nền
//                ) {
//                    Text(
//                        text = "List Fund",
//                        modifier = Modifier
//                            .border(2.dp, Color.Black, RoundedCornerShape(4.dp))
//                            .padding(8.dp), // Tạo khoảng cách giữa văn bản và đường viền
//
//                    )
//                }
//                Spacer(modifier = Modifier.padding(10.dp))
//            }
//        Column(
//            modifier = Modifier
//                .fillMaxWidth(),
//            verticalArrangement = Arrangement.SpaceBetween,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
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
                            itemOnClick = { navController.navigate("${Route.EditFundScreen.route}/${fund!!.fundId}") },
                            backgroundColor = Color.DarkGray.copy(alpha = 0.1f),
                            amountType = "EXPENSE: ",
                            surfaceColor = Color.Blue.copy(alpha = 0.5f)
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