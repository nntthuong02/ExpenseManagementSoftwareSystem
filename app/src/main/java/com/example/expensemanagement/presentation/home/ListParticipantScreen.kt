package com.example.expensemanagement.presentation.home

import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.presentation.home.component.AddEntity
import com.example.expensemanagement.presentation.home.component.DetailEntityItem
import com.example.expensemanagement.presentation.navigation.Route
import kotlinx.coroutines.launch

@Composable
fun ListParticipantScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val allPar by homeViewModel.allParticipant.collectAsState()
    val transByPar by homeViewModel.transByPar.collectAsState()
    val expense by homeViewModel.expense.collectAsState()
    val income by homeViewModel.income.collectAsState()
    val parAndExpense by homeViewModel.parAndExpense.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val parTitle by remember { mutableStateOf(homeViewModel.parName) }
    val parNameTextField = TextFieldValue(parTitle.collectAsState().value)

    LaunchedEffect(Unit){
        launch { homeViewModel.getAllPars() }
        launch { homeViewModel.fetchParAndExpense() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            // Chiếm 10% không gian
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            AddEntity(nameEntity = "Participant", name = parNameTextField.text, onNameChange = {
                homeViewModel.setParName(it)
            }) {
                coroutineScope.launch {
                    if (parNameTextField.text.isEmpty()) {
                        // Hiển thị Snackbar thông báo lỗi
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please enter name")
                        }
                    } else {
                        homeViewModel.insertParticipant(parTitle.value)
                        navController.navigateUp()
                        navController.navigate("${Route.ListParticipantScreen.route}")
                        Toast.makeText(context, "Add successfully", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            Box(
                modifier = Modifier
                    .background(Color.Green) // Màu nền cho văn bản
                    .padding(0.dp) // Khoảng cách giữa văn bản và viền nền
            ) {
                Text(
                    text = "List Participant",
                    modifier = Modifier
                        .border(2.dp, Color.Black, RoundedCornerShape(4.dp))
                        .padding(8.dp), // Tạo khoảng cách giữa văn bản và đường viền

                )
            }
            Spacer(modifier = Modifier.padding(10.dp))
        }
//        Column(
//            modifier = Modifier
//                .fillMaxWidth(),
//            verticalArrangement = Arrangement.SpaceBetween,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp, // Độ cao của Surface
            shape = RoundedCornerShape(16.dp)
        ) {

            LazyColumn {
//                    stickyHeader {
//                        Column(
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Text(
//                                text = "List Participant",
//                                modifier = Modifier
//                                    .border(2.dp, Color.Black, RoundedCornerShape(4.dp))
//                                    .padding(8.dp) // Tạo khoảng cách giữa văn bản và đường viền
//                            )
//                            Spacer(modifier = Modifier.padding(10.dp))
//                        }
//                    }


                itemsIndexed(parAndExpense) { _, (par, expense) ->
//                    coroutineScope.launch {
//                        homeViewModel.getTransactionByPar(par.participantId)
//                        var sumExpense = 0.0
//                        var sumIncome = 0.0
//                        transByPar.forEach { trans ->
//                            if (trans.transactionType == Constants.EXPENSE) {
//                                sumExpense += trans.amount
//                            } else {
//                                sumIncome += trans.amount
//                            }
//                        }
//                        homeViewModel.setIncome(sumIncome)
//                        homeViewModel.setExpense(sumExpense)
//                    }
                    DetailEntityItem(
                        name = par.participantName,
                        numberTransaction = transByPar.size,
                        amount = expense,
                        itemOnClick = { navController.navigate("${Route.EditParticipantScreen.route}/${par!!.participantId}") },
                        backgroundColor = Color.DarkGray.copy(alpha = 0.3f),
                        amountType = "Expense: ",
                        surfaceColor = Color.Blue.copy(alpha = 0.8f)
                    )

                }
            }
            SnackbarHost(hostState = snackbarHostState)

        }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }


    }
}