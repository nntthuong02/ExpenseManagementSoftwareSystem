//package com.example.expensemanagement.presentation.home.component
//
//import androidx.compose.animation.AnimatedContent
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.expensemanagement.domain.models.Fund
//import com.example.expensemanagement.domain.models.Participant
//import com.example.expensemanagement.presentation.common.TabContent
//
//
//@Composable
//fun TabContent(
//    fund: Fund,
//    par: Participant,
//    selectedTab: TabContent
//) {
//    AnimatedContent(targetState = selectedTab) { targetTab ->
//        when (targetTab) {
//            TabContent.PARTICIPANT -> {
//                EditNameEntity(nameEntity = "participant", name = par.participantName, onNameChange = ()) {
//
//                }
//            }
//            TabContent.FUND -> FundContent()
//            else -> TransContent()
//        }
//    }
//}
//
//@Composable
//fun ParContent(
//    parName: String,
//    onNameChange: (String) -> Unit,
//    onSaveClick: () -> Unit
//) {
//    // Content for Tab 1
//    EditNameEntity(
//        "participant",
//        name = parName,
//        onNameChange()
//
//    )
//}
//
//@Composable
//fun FundContent() {
//    // Content for Tab 2
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(text = "Content for Tab 2", style = MaterialTheme.typography.headlineMedium)
//        // Add more content here
//    }
//}
//@Composable
//fun TransContent() {
//    // Content for Tab 2
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(text = "Content for Tab 2", style = MaterialTheme.typography.headlineMedium)
//        // Add more content here
//    }
//}
