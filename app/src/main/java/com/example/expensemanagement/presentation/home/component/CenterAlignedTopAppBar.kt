package com.example.expensemanagement.presentation.home.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenterAlignedTopAppBar(
    showSnackbarText: String,
    name: String,
    rightIcon1: Int,
    rightIcon2: Int,
    iconOnclick1: () -> Unit,
    iconOnlick2: () -> Unit,
    showIconRight1: Boolean,
    showIconRight2: Boolean,
    showIconLeft: Boolean,
    navController: NavHostController,
    showSnackbar: MutableState<Boolean>,
    content: @Composable (PaddingValues) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    if (showIconLeft){
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                },
                actions = {
                    Row(Modifier.padding(5.dp)) {
                        if (showIconRight2){
                            IconButton(onClick = iconOnlick2) {
                                Icon(
                                    painter = painterResource(id = rightIcon2),
                                    contentDescription = "Localized description"
                                )
                            }
                        }
                        if (showIconRight1){
                            IconButton(onClick = iconOnclick1) {
                                Icon(
                                    painter = painterResource(id = rightIcon1),
                                    contentDescription = "Localized description"
                                )
                            }
                        }

                    }

                },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            if(showSnackbar.value){
                scope.launch {
                    snackbarHostState.showSnackbar(
                        showSnackbarText
                    )
                    showSnackbar.value = false
                }
            }
        },
        content = content
    )
}

@Composable
fun ScrollContent(modifier: Modifier = Modifier){
    Text(text = "test")
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun Preview(){
    val content: @Composable (PaddingValues) -> Unit = {
        Text(text = "test", modifier = Modifier.padding(paddingValues = it))
    }
    val showSnackbar = remember { mutableStateOf(false) }
    showSnackbar.value = false
    CenterAlignedTopAppBar(
        showSnackbarText = "test",
        name = "Fund",
        rightIcon1 = R.drawable.edit_square_24px,
        rightIcon2 = R.drawable.delete_24px,
        iconOnclick1 = { /*TODO*/ },
        iconOnlick2 = {},
        showIconLeft = true,
        showIconRight1 = true,
        showIconRight2 = true,
        showSnackbar = showSnackbar,
        content = content,
        navController = rememberNavController()
    )
}