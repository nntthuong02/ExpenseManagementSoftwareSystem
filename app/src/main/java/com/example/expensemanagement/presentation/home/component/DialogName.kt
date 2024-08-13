package com.example.expensemanagement.presentation.home.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensemanagement.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogName(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    name: String,
    onNameChange: (String) -> Unit,
    dialogTitle: String,
    dialogText: String,
    iconId: Int,
) {
    AlertDialog(
        icon = {
            Icon(painter = painterResource(id = iconId), contentDescription = "Example Icon")
        },
        title = {
            Text(
                text = dialogTitle,
            )
        },
        text = {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                value = name,
                onValueChange = onNameChange,
                label = { Text("Enter $dialogText name") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )
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
                Text("Cancel")
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun PreviewDialogAddName(){
    DialogName(
        onDismissRequest = { /*TODO*/ },
        onConfirmation = { /*TODO*/ },
        name = "Quy 2",
        onNameChange = {},
        dialogTitle = "Enter the name of the fund you want to create!",
        dialogText = "fund",
        iconId = R.drawable.post_add_24px
    )
}