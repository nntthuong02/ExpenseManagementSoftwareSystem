package com.example.expensemanagement.presentation.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.expensemanagement.presentation.home.component.ScrollContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Test1() {
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
                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Text(
                                "Medium Top App Bar1",
                                color = Color.Red,
//                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }


                        // Luôn hiển thị Text chính
                        Text(
                            "Medium Top App Bar",
//                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomPositionAlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    alignment: Alignment = Alignment.TopEnd // Đặt mặc định là hiển thị bên trái
) {
    Box(
        modifier = Modifier.fillMaxSize(),  // Chiếm toàn bộ không gian màn hình
        contentAlignment = alignment  // Định vị AlertDialog theo Alignment
    ) {
        AlertDialog(
            modifier = Modifier.wrapContentSize(),  // Giữ nguyên kích thước của AlertDialog
            icon = {
                Icon(icon, contentDescription = "Example Icon")
            },
            title = {
                Text(text = dialogTitle)
            },
            text = {
                Text(text = dialogText)
            },
            onDismissRequest = {
                onDismissRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmation()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun preview(){
    Test1()
}

@Preview(showSystemUi = true)
@Composable
fun preview2(){
    CustomPositionAlertDialogExample(
        onDismissRequest = { /*TODO*/ },
        onConfirmation = { /*TODO*/ },
        dialogTitle = "",
        dialogText = "",
        icon = Icons.Filled.Delete
    )
}