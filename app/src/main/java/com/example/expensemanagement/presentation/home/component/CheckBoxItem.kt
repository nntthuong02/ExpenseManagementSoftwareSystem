package com.example.expensemanagement.presentation.home.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp

@Composable
fun CheckBoxItem(
    name: String,
    check: Boolean,
    onCheckChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
            .height(IntrinsicSize.Min)
            .border(1.dp, Color.Black)
    ) {
        Box(modifier = Modifier
            .weight(0.7f)
            .padding(0.dp)
            .align(Alignment.CenterVertically)
        ){
            Text(
                text = name,
                modifier = Modifier.padding(10.dp)
                    .fillMaxWidth()
            )
        }
        Divider(
            color = Color.Black,
            modifier = Modifier.fillMaxHeight().width(1.dp)
        )
        Box(modifier = Modifier
            .weight(0.3f)
            .padding(0.dp)
        ){
            Checkbox(
                modifier = Modifier.align(Alignment.Center),
                checked = check,
                onCheckedChange = { isChecked ->
                    onCheckChange(isChecked)
                }
            )
        }
    }

}