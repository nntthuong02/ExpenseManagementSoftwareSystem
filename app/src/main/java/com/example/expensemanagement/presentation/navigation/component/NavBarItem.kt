package com.example.expensemanagement.presentation.navigation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensemanagement.R
import com.example.expensemanagement.ui.theme.ExpenseManagementTheme


@ExperimentalAnimationApi
@Composable
fun NavBarItem(item: NavBarItemHolder, onClick: () -> Unit, selected: Boolean) {

    val backgroundColor =
        if (selected) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f) else Color.Transparent
    val contentColor =
        if (selected) MaterialTheme.colorScheme.onSurface else Color.Gray

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Icon(
                painterResource(id = item.icon),
                tint = contentColor,
                contentDescription = item.title
            )
                Text(
                    text = item.title,
                    textAlign = TextAlign.Center,
                    color = contentColor,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
        }
    }
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun NavBarItemPreview() {
    val item = NavBarItemHolder(
        title = "Home",
        icon = R.drawable.home_24px,
        route = "home"
    )
    val selected = remember { mutableStateOf(true) }

    ExpenseManagementTheme {
        NavBarItem(
            item = item,
            onClick = { selected.value = !selected.value },
            selected = selected.value
        )
    }
}