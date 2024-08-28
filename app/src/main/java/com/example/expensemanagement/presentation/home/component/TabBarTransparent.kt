package com.example.expensemanagement.presentation.home.component

import androidx.compose.foundation.background
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
import androidx.compose.material3.SegmentedButtonColors
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensemanagement.presentation.common.TabContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabBarTransparent(
    tab1: TabContent,
    tab2: TabContent,
    selectedTab: TabContent,
    onTabSelected: (TabContent) -> Unit
) {

    SingleChoiceSegmentedButtonRow() {
        val transparentButtonColors = SegmentedButtonDefaults.colors().copy(
            activeContainerColor = Color.Transparent,
            inactiveContainerColor = Color.Transparent,
            activeBorderColor = Color.Transparent,
            inactiveBorderColor = Color.Transparent,
        )
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
            onClick = { onTabSelected(tab1) },
            selected = selectedTab == tab1,
            colors = transparentButtonColors
        ) {
            Text(
                tab1.title,
                fontFamily = FontFamily.Cursive,
                style = if (selectedTab == tab1) {
                    TextStyle(fontWeight = FontWeight.Bold)
                } else {
                    TextStyle(fontWeight = FontWeight.Normal, color = Color.Gray)
                }
            )
        }

        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            onClick = { onTabSelected(tab2) },
            selected = selectedTab == tab2,
            colors = transparentButtonColors
        ) {
            Text(
                tab2.title,
                fontFamily = FontFamily.Cursive,
                style = if (selectedTab == tab2) {
                    TextStyle(fontWeight = FontWeight.Bold)
                } else {
                    TextStyle(fontWeight = FontWeight.Normal, color = Color.Gray)
                }
            )
        }

    }
}


@Preview(showSystemUi = true)
@Composable
fun TabBarTransparentPre() {
    TabBarTransparent(tab1 = TabContent.FUND, tab2 = TabContent.PARTICIPANT, selectedTab = TabContent.FUND, onTabSelected = {})
}
