package com.example.expensemanagement.presentation.home

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.expensemanagement.R
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.domain.models.Transaction
import com.example.expensemanagement.presentation.common.TabContent
import com.example.expensemanagement.presentation.home.component.CenterAlignedTopAppBar
import com.example.expensemanagement.presentation.home.component.TabBar
import com.example.expensemanagement.presentation.home.component.TransItem
import com.example.expensemanagement.presentation.home.component.TransactionParFundItem
import com.example.expensemanagement.presentation.insight_screen.component.AlertDialogComponent
import com.example.expensemanagement.presentation.insight_screen.component.getCategory
import com.example.expensemanagement.presentation.navigation.Route
import kotlinx.coroutines.launch

@Composable
fun ListTransactionScreen(
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val unpaidTrans by homeViewModel.mapUnpaidTrans.collectAsState()
    val paidTrans by homeViewModel.mapPaidTrans.collectAsState()
    val currencyCode by homeViewModel.selectedCurrencyCode.collectAsState()

    val selectedTab by homeViewModel.tabPaidTrans.collectAsState()
    val context = LocalContext.current
    val deleteUnpaidDialog = remember { mutableStateOf(false) }
    val deletePaidDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit){
        launch { homeViewModel.fetchAllTransactions() }
        launch { homeViewModel.fetchUnpaidTransactions() }
        launch { homeViewModel.fetchPaidTransactions() }
    }

    CenterAlignedTopAppBar(
        showSnackbarText = "",
        name = "Transaction",
        rightIcon1 = R.drawable.delete_24px,
        rightIcon2 = R.drawable.credit_card_off_24px,
        iconOnclick1 = { deletePaidDialog.value = true },
        iconOnlick2 = { deleteUnpaidDialog.value = true },
        showIconRight1 = true,
        showIconRight2 = true,
        showIconLeft = true,
        navController = navController,
        showSnackbar = mutableStateOf(false)
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding),
//            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (deleteUnpaidDialog.value){
                AlertDialogComponent(
                    onDismissRequest = { deleteUnpaidDialog.value = false },
                    onConfirmation = {
                        if (unpaidTrans.isEmpty()){
                            Toast.makeText(context, "There are no transactions to delete!", Toast.LENGTH_SHORT).show()
                        } else {
                            navController.navigate(Route.ListTransaction.route)
                            coroutineScope.launch {
                                homeViewModel.eraseUnpaidTrans()
                            }
                            Toast.makeText(context, "Successfully deleted unpaid transactions", Toast.LENGTH_SHORT)
                        }
                        deleteUnpaidDialog.value = false
                                     },
                    dialogTitle = "Delete unpaid transaction",
                    dialogText = "Are you sure you want to delete unpaid transactions?",
                    icon = Icons.Filled.Delete
                )
            }

            if (deletePaidDialog.value){
                AlertDialogComponent(
                    onDismissRequest = { deletePaidDialog.value = false },
                    onConfirmation = {
                        if (unpaidTrans.isEmpty()){
                            Toast.makeText(context, "There are no transactions to delete!", Toast.LENGTH_SHORT).show()
                        } else {
                            navController.navigate(Route.ListTransaction.route)
                            coroutineScope.launch {
                                homeViewModel.erasePaidTrans()
                            }
                            Toast.makeText(context, "Successfully deleted paid transactions", Toast.LENGTH_SHORT)
                        }
                        deletePaidDialog.value = false
                    },
                    dialogTitle = "Delete the paid transactions",
                    dialogText = "Are you sure you want to delete the paid transactions?",
                    icon = Icons.Filled.Delete
                )
            }

            TabBar(tab1 = TabContent.UNPAID, tab2 = TabContent.Paid, selectedTab = selectedTab, onTabSelected = {
                tabContent ->
                homeViewModel.setTabPaidTrans(tabContent)
            })

            AnimatedContent(targetState = selectedTab, label = "Transaction") {targetState ->
                when(targetState){
                    TabContent.UNPAID -> TransContent(
                        currencyCode = currencyCode,
                        listTrans = unpaidTrans,
                        onItemClick = {}
                    )
                    else -> TransContent(currencyCode = currencyCode, listTrans = paidTrans, onItemClick = {})
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransContent(
    currencyCode: String,
    listTrans: Map<String, List<Pair<Transaction, Pair<Participant, Fund>>>>,
    onItemClick: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.padding(
            top = 5.dp
        )
            .fillMaxWidth()
    ) {
        if (listTrans.isEmpty()) {
            Box(
                modifier = Modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                    .padding(0.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Not Transaction",
                    modifier = Modifier

                        .align(Alignment.Center)
                        .padding(8.dp),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,

                    )
            }

        } else {
            LazyColumn(
                Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(
                    start = 5.dp,
                    top = 5.dp,
                    end = 5.dp
                )
            ) {
                listTrans.forEach{(date, listPair) ->
                    stickyHeader{
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Text(
                                text = date,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            )
                        }
                    }



                    itemsIndexed(listPair){index, (trans, parFund) ->
                        val category = getCategory(trans.category)

                        TransactionParFundItem(
                            transaction = trans,
                            category = category,
                            currencyCode = currencyCode,
                            fundName = parFund.second.fundName,
                            parName = parFund.first.participantName
                        )
                    }
                }
//                itemsIndexed(listTrans) { index, list ->
//                    val category = getCategory(trans.category)
//
//                    TransItem(
//                        transaction = trans,
//                        category = category,
//                        currencyCode = currencyCode,
//                        parName = participant.participantName,
//
//                        )
//
//                }
            }
        }
    }
}