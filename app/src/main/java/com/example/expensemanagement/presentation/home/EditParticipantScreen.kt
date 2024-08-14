package com.example.expensemanagement.presentation.home

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.expensemanagement.R
import com.example.expensemanagement.common.Constants
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.domain.models.Transaction
import com.example.expensemanagement.presentation.common.Category
import com.example.expensemanagement.presentation.common.TabContent
import com.example.expensemanagement.presentation.home.component.CategoryChart
import com.example.expensemanagement.presentation.home.component.CenterAlignedTopAppBar
import com.example.expensemanagement.presentation.home.component.DialogName
import com.example.expensemanagement.presentation.home.component.EditNameEntity
import com.example.expensemanagement.presentation.home.component.TabBar
import com.example.expensemanagement.presentation.home.component.TransItem
import com.example.expensemanagement.presentation.insight_screen.component.AlertDialogComponent
import com.example.expensemanagement.presentation.insight_screen.component.getCategory
import com.example.expensemanagement.presentation.navigation.Route
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun EditParticipantScreen(
    parId: Int,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val selectedTab by homeViewModel.tabPar.collectAsState()
    val parById by homeViewModel.parById.collectAsState()
    val transByPar by homeViewModel.transByPar.collectAsState()
    val currencyCode by homeViewModel.selectedCurrencyCode.collectAsState()
    val transWithFund by homeViewModel.transWithFund.collectAsState()
    val categoryAndExpense by homeViewModel.categoryAndExpenseByPar.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val parTitle by remember { mutableStateOf(homeViewModel.parName) }
    val showSnack = remember { mutableStateOf(false) }
    val showSnackbarText = remember { mutableStateOf("") }
    val openAlertDialog = remember { mutableStateOf(false) }
    val openDelelteDialog = remember { mutableStateOf(false) }
    val parNameFieldValue = TextFieldValue(parTitle.collectAsState().value)
    val showContent = remember { mutableStateOf(false) }


    LaunchedEffect(parId) {
        homeViewModel.apply {
            getParById(parId)
            getTransactionByPar(parId)
            getTransWithFundByPar()
            getExpenseCategoryByPar(parId)
        }
    }
    if (parById != null) {
        showContent.value = true
    }
    AnimatedContent(targetState = showContent.value, label = "VisibilityAnimation") { isVisible ->
        if (isVisible) {
            CenterAlignedTopAppBar(
                showSnackbarText = showSnackbarText.value,
                name = parById!!.participantName,
                rightIcon1 = R.drawable.edit_square_24px,
                rightIcon2 = R.drawable.delete_24px,
                iconOnclick1 = { openAlertDialog.value = true },
                iconOnlick2 = { openDelelteDialog.value = true },
                showIconRight1 = true,
                showIconRight2 = true,
                showIconLeft = true,
                navController = navController,
                showSnackbar = showSnack
            ) { innerPadding ->
                if (openAlertDialog.value == true) {
                    DialogName(
                        onDismissRequest = { openAlertDialog.value = false },
                        onConfirmation = {
                            if (parNameFieldValue.text.isEmpty()) {
                                openAlertDialog.value = false
                                showSnackbarText.value = "Please enter name"
                                showSnack.value = true
                            } else {
                                openAlertDialog.value = false
                                coroutineScope.launch {
                                    homeViewModel.apply {
                                        updateParticipantById(parId, parName.value)
                                        navController.navigate("${Route.EditParticipantScreen.route}/${parById!!.participantId}")
                                        Toast.makeText(
                                            context,
                                            "Participant name updated successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                        },
                        name = parNameFieldValue.text,
                        onNameChange = { text ->
                            homeViewModel.setParName(text)
                        },
                        dialogTitle = "Enter the name of the participnat you want to create!",
                        dialogText = "participant",
                        iconId = R.drawable.post_add_24px
                    )
                }

                if (openDelelteDialog.value) {
                    AlertDialogComponent(
                        onDismissRequest = { openDelelteDialog.value = false },
                        onConfirmation = {
                            if (parId != 1) {
                                openDelelteDialog.value = false
                                navController.navigateUp()
                                Toast.makeText(context, "erase successfully", Toast.LENGTH_SHORT)
                                    .show()
                                coroutineScope.launch {
                                    homeViewModel.eraseParById(parId)
                                }
                            } else {
                                openDelelteDialog.value = false
                                showSnackbarText.value = "Cannot delete default participant!"
                                showSnack.value = true
                            }
                        },
                        dialogTitle = "Confirm deletion",
                        dialogText = "Are you sure you want to delete this participant?",
                        icon = Icons.Filled.Delete
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues = innerPadding)

                ) {
                    TabBar(
                        tab1 = TabContent.PARTICIPANT,
                        tab2 = TabContent.TRANSACTION,
                        selectedTab = selectedTab
                    ) { tabContent ->
                        homeViewModel.setTabPar(tabContent)
                    }
//        content()
                    AnimatedContent(
                        targetState = selectedTab,
                        label = "Participant Content"
                    ) { targetTab ->
                        when (targetTab) {
                            TabContent.PARTICIPANT -> ParticipantContent(
                                categoryChart = categoryAndExpense,
                            )

                            else -> {
                                TransactionContent2(
                                    currencyCode = currencyCode,
                                    transByPar = transByPar,
                                    transWithFund = transWithFund,
                                    onItemClick = {}
                                )
                            }
                        }
                    }
                }
                //Column
            }
        }
    }

}

@Composable
fun ParticipantContent(
    categoryChart: List<Pair<Category, Double>>
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        if (categoryChart.isNotEmpty()) {
            CategoryChart(oxLabel = "Category", oyLabel = "Money", chartData = categoryChart)
            Spacer(modifier = Modifier.padding(10.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "The chart shows the total amount of expenses (Unit: thousands)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }
        } else {
            CategoryChart(oxLabel = "Category", oyLabel = "Money", chartData = categoryChart)
            Spacer(modifier = Modifier.padding(10.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "The chart shows the total amount of expenses (Unit: thousands)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily.SansSerif
                    )
                )
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionContent2(
    currencyCode: String,
    transByPar: List<Transaction>,
    transWithFund: List<Pair<Transaction, Fund>>,
    onItemClick: (Int) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.padding(
            top = 5.dp
        )
    ) {
        if (transByPar.isEmpty()) {
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
                contentPadding = PaddingValues(
                    start = 5.dp,
                    top = 5.dp,
                    end = 5.dp
                )
            ) {
                itemsIndexed(transWithFund) { index, (trans, fund) ->
                    val category = getCategory(trans.category)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Text(
                            text = getFormattedDate(trans.date),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        )
                    }
                    TransItem(
                        transaction = trans,
                        category = category,
                        currencyCode = currencyCode,
                        parName = fund.fundName,

                        )

                }
            }
        }


    }
}

private  fun getFormattedDate(date: Date): String {
    return SimpleDateFormat("yyyy-MM-dd").format(date)
}

@Preview(showSystemUi = true)
@Composable
fun ParticipantContentPreview() {
    val category = listOf(
        Pair(Category.CLOTHES, 10.0)
    )

    ParticipantContent(
        categoryChart = category,
    )
}
