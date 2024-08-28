package com.example.expensemanagement.presentation.insight_screen.component

import androidx.compose.runtime.Composable
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import java.util.Date
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseDate(
    onDismiss: () -> Unit = {},
    onConfirm: (Date) -> Unit = {}
) {
    // TODO demo how to read the selected date from the state.

        val datePickerState = rememberDatePickerState()
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                onDismiss()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val timestampMillis = datePickerState.selectedDateMillis
                        val selectedDate = timestampMillis?.let { Date(it) }
                        onConfirm(selectedDate!!)
                        onDismiss()
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }

}


@Preview(showSystemUi = true)
@Composable
fun EditTranPreview(){
    ChooseDate()
}