package com.example.expensemanagement.presentation.transaction_screen.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensemanagement.R
import com.example.expensemanagement.domain.models.Fund
import com.example.expensemanagement.domain.models.Participant
import com.example.expensemanagement.presentation.transaction_screen.TransactionViewModel

@Composable
fun ParticipantTag(
    participant: Participant,
    onParSelected: (Participant) -> Unit,
    transactionViewModel: TransactionViewModel = hiltViewModel()
) {
    val selectedParticipant by transactionViewModel.selectedParticipant.collectAsState()
    val isSelected = selectedParticipant == participant

    TextButton(
        onClick = {
            transactionViewModel.selectParticipant(participant)
            onParSelected(selectedParticipant!!)
        },
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(
            horizontal = 5.dp,
            vertical = 5.dp
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            else Color.Transparent,
            contentColor = if (isSelected)
                Color.White else
                MaterialTheme.colorScheme.primary
        ),
    ) {
        Icon(
            painter = painterResource(
                id = if (isSelected)
                    R.drawable.check_24px else R.drawable.person_24px
            ),
            contentDescription = participant.participantName,
        )

        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = participant.participantName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ParticipantTagPreview() {
    val participant = Participant(1, "Thuong")
    ParticipantTag(participant = participant, onParSelected = {})
}