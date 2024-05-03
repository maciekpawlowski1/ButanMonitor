package com.pawlowski.butanmonitor.ui.components.error

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorItem(onRetryClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Something went wrong",
            textAlign = TextAlign.Center,
        )
        Button(onClick = onRetryClick) {
            Text(text = "Reload")
        }
    }
}
