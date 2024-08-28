//package com.example.expensemanagement.presentation.home.component
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.material3.Checkbox
//import androidx.compose.material3.Text
//import androidx.compose.material3.TriStateCheckbox
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.state.ToggleableState
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.expensemanagement.domain.models.Participant
//import kotlinx.coroutines.flow.MutableStateFlow
//
//
//@Composable
//fun CheckBoxParent(
//    allParticipant: List<Participant>,
//    initialListPar: List<Participant>,
//    childCheckedStates: List<Boolean>,
//    parentState: ToggleableState,
//    parentOnClick: () -> Unit,
//    onCheckedChange: (Int, Boolean) -> Unit
//) {
//    // Initialize states for the child checkboxes
////    val childCheckedStates = remember { mutableStateListOf(false, false, false) }
//    // Compute the parent state based on children's states
////    val parentState = when {
////        childCheckedStates.all { it } -> ToggleableState.On
////        childCheckedStates.none { it } -> ToggleableState.Off
////        else -> ToggleableState.Indeterminate
////    }
//
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Parent TriStateCheckbox
//        Text("Add participant")
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text("Select all")
//            TriStateCheckbox(
//                state = parentState,
//                onClick = parentOnClick
////                {
////                    // Determine new state based on current state
////                    val newState = parentState != ToggleableState.On
////                    childCheckedStates.forEachIndexed { index, _ ->
////                        childCheckedStates[index] = newState
////                    }
////                }
//            )
//        }
//        LazyColumn{
//            itemsIndexed(childCheckedStates) { index, checked ->
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    Text("${allParticipant[index].participantName}     ")
//                    Checkbox(
//                        checked = checked,
//                        onCheckedChange = { isChecked ->
//                            onCheckedChange(index, isChecked)
//                        }
////                    { isChecked ->
////                        // Update the individual child state
////                        childCheckedStates[index] = isChecked
////                    }
//                    )
//                }
//            }
//        }
//        // Child Checkboxes
//
//    }
//
//    if (childCheckedStates.all { it }) {
//        Text("All options selected")
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun CheckBoxParentPreview() {
//    // Sample participants for preview
//    val allParticipant = listOf(
//        Participant(1, "Participant 1"),
//        Participant(2, "Participant 2"),
//        Participant(3, "Participant 3")
//    )
//
//    val initialListPar = listOf(
//        Participant(1, "Participant 1"),
//        Participant(3, "Participant 3")
//    )
//
//    // Sample child checked states
//    val childCheckedStates = listOf(false, false, false)
//
//    CheckBoxParent(
//        allParticipant = allParticipant,
//        initialListPar = initialListPar,
//        childCheckedStates = childCheckedStates,
//        parentOnClick = { /* Do nothing for preview */ },
//        onCheckedChange = { index, isChecked -> /* Do nothing for preview */ }
//    )
//}
