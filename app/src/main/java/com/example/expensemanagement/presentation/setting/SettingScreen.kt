package com.example.expensemanagement.presentation.setting

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.presentation.home.component.CenterAlignedTopAppBar
import com.example.expensemanagement.presentation.home.component.CheckBoxItem
import com.example.expensemanagement.presentation.home.component.ParBarChart
import com.example.expensemanagement.presentation.home.component.ScrollContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
//    modifier: Modifier = Modifier
) {
// Initialize states for the child checkboxes
    val childCheckedStates = remember { mutableStateListOf(false, false, false) }

    // Compute the parent state based on children's states
    val parentState = when {
        childCheckedStates.all { it } -> ToggleableState.On
        childCheckedStates.none { it } -> ToggleableState.Off
        else -> ToggleableState.Indeterminate
    }

    Column(modifier = Modifier.padding(0.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .border(1.dp, Color.Black)
            ) {
                Box(modifier = Modifier
                    .weight(0.7f)
                    .padding(0.dp)
                    .align(Alignment.CenterVertically)
                ){
                    Text(
                        text = "Select all",
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    )
                }
                Divider(
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )
                Box(modifier = Modifier
                    .weight(0.3f)
                    .padding(0.dp)
//                    .align(Alignment.CenterVertically)
                ){
                    TriStateCheckbox(
                        modifier = Modifier
                            .padding(0.dp)
                            .align(Alignment.Center),
                        state = parentState,
                        onClick = {
                            // Determine new state based on current state
                            val newState = parentState != ToggleableState.On
                            childCheckedStates.forEachIndexed { index, _ ->
                                childCheckedStates[index] = newState
                            }
                        }
                    )
                }
            }
            Text("Select all")

        }

        // Child Checkboxes
        childCheckedStates.forEachIndexed { index, checked ->
            CheckBoxItem(name = "Option ${index + 1}", check = checked){isCheck ->
                childCheckedStates[index] = isCheck
            }
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                Text("Option ${index + 1}")
//                Checkbox(
//                    checked = checked,
//                    onCheckedChange = { isChecked ->
//                        // Update the individual child state
//                        childCheckedStates[index] = isChecked
//                    }
//                )
//            }
        }
    }

    if (childCheckedStates.all { it }) {
        Text("All options selected")
    }

}

@Preview(showSystemUi = true)
@Composable
fun Preview(){
    SettingScreen()
}