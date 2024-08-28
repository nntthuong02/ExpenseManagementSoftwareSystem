package com.example.expensemanagement.presentation.home.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensemanagement.presentation.common.TabContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBar(
    tab1: TabContent,
    tab2: TabContent,
    selectedTab: TabContent,
    onTabSelected: (TabContent) -> Unit
) {

    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
            onClick = { onTabSelected(tab1) },
            selected = selectedTab == tab1
        ) {
            Text(tab1.title)
        }

        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            onClick = { onTabSelected(tab2) },
            selected = selectedTab == tab2
        ) {
            Text(tab2.title)
        }

    }
}


@Preview(showSystemUi = true)
@Composable
fun TabBarItem() {
    TabBar(tab1 = TabContent.FUND, tab2 = TabContent.TRANSACTION, selectedTab = TabContent.FUND, onTabSelected = {})
}
