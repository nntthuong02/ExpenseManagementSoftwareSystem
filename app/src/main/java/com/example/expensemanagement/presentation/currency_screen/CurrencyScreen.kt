package com.example.expensemanagement.presentation.currency_screen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.domain.models.CurrencyModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CurrencyScreen(
    navController: NavController,
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {
    val currencies by currencyViewModel.countryCurrencies
    var selectedCountry by remember { mutableStateOf(CurrencyModel()) }
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            if(selectedCountry != CurrencyModel()){
                ExtendedFloatingActionButton(
                    text = { Text("Set currency") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                    onClick = {
                        showBottomSheet = true
                    }
                )}
        }
    ) { // Column được hiển thị bên trong Scaffold không bị che khuất bởi các thành phần giao diện khác như FloatingActionButton
            contentPadding ->
        // Screen content
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CurrencyHeader()
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(paddingValues = contentPadding)
                ) {
                    currencies.forEach { (initial, countriesForInitial) ->
                        stickyHeader {
                            //sticky Header
                            Surface(color = MaterialTheme.colorScheme.background) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(
                                            start = 5.dp,
                                            end = 5.dp,
                                            top = 5.dp
                                        )
                                ) {
                                    Text(
                                        text = initial.toString(),
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )
                                }
                            }
                        }
                        //List
                        items(countriesForInitial.size) { index ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable {
                                        selectedCountry = if (selectedCountry != countriesForInitial[index]) {
                                            countriesForInitial[index]
                                        } else {
                                            CurrencyModel()
                                        }
                                    }
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal =5.dp,
                                        vertical = 5.dp
                                    )
                            ) {
                                //Text Button
                                TextButton(
                                    onClick = {
                                        selectedCountry = if (selectedCountry != countriesForInitial[index]) {
                                            countriesForInitial[index]
                                        } else {
                                            CurrencyModel()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selectedCountry == countriesForInitial[index])
                                            MaterialTheme.colorScheme.primary
                                        else Color.DarkGray.copy(alpha = 0.1f),
                                        contentColor = if (selectedCountry == countriesForInitial[index])
                                            contentColorFor(MaterialTheme.colorScheme.primary)
                                        else MaterialTheme.colorScheme.onSurface
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(20.dp)
                                ){
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(
                                                style = SpanStyle(
                                                    fontWeight = FontWeight.W600,
                                                    fontFamily = FontFamily.SansSerif,
                                                    fontSize = 14.sp
                                                )
                                            ) {
                                                append(countriesForInitial[index].country.uppercase())
                                            }

                                            withStyle(
                                                style = SpanStyle(
                                                    color = Color.DarkGray.copy(alpha = 0.5f),
                                                    fontWeight = FontWeight.Normal,
                                                    fontFamily = FontFamily.SansSerif,
                                                    fontSize = 14.sp
                                                )
                                            ) {
                                                append(" (${countriesForInitial[index].currencyCode})")
                                            }
                                        },
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Start
                                    )
                                }
                                //Text Button
                            }
                        }
                    }
                }
            }
            // Screen content
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    // Sheet content
                    Button(
                        //systemBarsPadding giup button khong bi thanh dieu huong che khuat
                        modifier = Modifier.systemBarsPadding(),
                        onClick = {
                            Log.d("test", "test1")
                            currencyViewModel.saveCurrency(selectedCountry.currencyCode)
                            Log.d("MyApp", "saveCurrency executed")
                            navController.popBackStack()
                            Log.d("MyApp", "popBackStack executed")
                            currencyViewModel.createEntity()
                            Log.d("MyApp", "createParticipants executed")
                            currencyViewModel.saveOnboardingState(completed = true)
                            Log.d("saveOnboardingState", "saveOnboardingState executed")
                            //Them cancle button trong tuong lai
//                        coroutineScope.launch { sheetState.hide() }.invokeOnCompletion {
//                            if (!sheetState.isVisible) {
//                                showBottomSheet = false
//                            }
//                        }
                        }) {
                        Text("Commit Currency")
                    }
                }
                // Sheet content
            }
        }
    }
}

@Composable
fun CurrencyHeader(){
    Card(elevation = CardDefaults.cardElevation(defaultElevation = (1.dp))) {
        Text(
            text = "Set currency",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.W700),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 5.dp,
                    end = 5.dp,
                    top = 5.dp
                ),
            textAlign = TextAlign.Start
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyPreview(){
    CurrencyScreen(navController = rememberNavController())
}