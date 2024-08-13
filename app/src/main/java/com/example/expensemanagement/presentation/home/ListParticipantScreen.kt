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
import com.example.expensemanagement.R
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.presentation.home.component.AddEntity
import com.example.expensemanagement.presentation.home.component.CenterAlignedTopAppBar
import com.example.expensemanagement.presentation.home.component.DetailEntityItem
import com.example.expensemanagement.presentation.home.component.DialogName
import com.example.expensemanagement.presentation.navigation.Route
import kotlinx.coroutines.launch

@Composable
fun ListParticipantScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val parAndExpense by homeViewModel.parAndExpense.collectAsState()
    val numberTransOfPar by homeViewModel.numberTransOfParticipant.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val openAlertDialog = remember{ mutableStateOf(false) }
    val showSnack = remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val parTitle by remember { mutableStateOf(homeViewModel.parName) }
    val parNameTextField = TextFieldValue(parTitle.collectAsState().value)

    LaunchedEffect(Unit){
        launch { homeViewModel.getAllPars() }
        launch { homeViewModel.fetchParAndExpense() }
        launch { homeViewModel.getNumberTransOfPar() }
    }


    CenterAlignedTopAppBar(
        showSnackbarText = "Please enter name",
        name = "Participant",
        rightIcon1 = R.drawable.add_24px,
        rightIcon2 = 0,
        iconOnclick1 = { openAlertDialog.value = true },
        iconOnlick2 = {},
        showIconRight1 = true,
        showIconRight2 = false,
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
                                if (parNameTextField.text.isEmpty()){
                                    openAlertDialog.value = false
                                    showSnack.value = true
                                }else {
                                    openAlertDialog.value = false
                                    homeViewModel.insertParticipant(parTitle.value)
                                    navController.navigate(Route.ListParticipantScreen.route)
                                    Toast.makeText(context, "Additional participant successfully", Toast.LENGTH_SHORT)
                                }
                            }
                        },
                        name = parNameTextField.text,
                        onNameChange = {text ->
                            homeViewModel.setParName(text)
                        },
                        dialogTitle = "Enter the name of the participant you want to create!",
                        dialogText = "participant",
                        iconId = R.drawable.person_add_24px
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    LazyColumn {
                        itemsIndexed(parAndExpense) { _, (par, expense) ->
                            var numberTrans = 0
                            numberTransOfPar.forEach { (par2, number) ->
                                if(par.participantId == par2.participantId){
                                    numberTrans = number
                                }
                            }

                            DetailEntityItem(
                                name = par.participantName,
                                numberTransaction = numberTrans,
                                amount = homeViewModel.formatAmount(expense),
                                itemOnClick = { navController.navigate("${Route.EditParticipantScreen.route}/${par!!.participantId}") },
                                backgroundColor = Color.DarkGray.copy(alpha = 0.1f),
                                amountType = "Expense: ",
                                surfaceColor = Color.Blue.copy(0.5f)
                            )
                        }
                    }


                }
            }



    }

}