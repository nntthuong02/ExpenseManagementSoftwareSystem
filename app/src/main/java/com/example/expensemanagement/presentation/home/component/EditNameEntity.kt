package com.example.expensemanagement.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.expensemanagement.presentation.home.HomeViewModel

@Composable
fun EditNameEntity(
    nameEntity: String,
    name: String,
    onNameChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
//                .border(2.dp, Color.Black, RoundedCornerShape(4.dp))
                .background(Color.LightGray) // Màu nền cho văn bản
                .padding(0.dp) // Khoảng cách giữa văn bản và viền nền
                .fillMaxWidth()
        ) {
            Text(
                text = "$nameEntity",
                modifier = Modifier

                    .align(Alignment.Center)
                    .padding(8.dp), // Tạo khoảng cách giữa văn bản và đường viền
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,

            )}
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),
            value = name,
            onValueChange = onNameChange,
            label = { Text("Edit name $nameEntity:") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            onClick = onSaveClick,
//                modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Update name")
        }
        Spacer(modifier = Modifier.padding(5.dp))
//        Button(
//            modifier = Modifier
//                .fillMaxWidth(),
//            onClick = onSaveClick,
////                modifier = Modifier.align(Alignment.CenterHorizontally)
//        ) {
//            Text("Delete fund")
//        }


    }
}

@Preview(showSystemUi = true)
@Composable
fun EditNameEntityPreview() {
    EditNameEntity(
        "fund",
        "Quy 2",
        onNameChange = {},
        onSaveClick = {}
    )
}
