package com.pawlowski.butanmonitor.ui.screen.chooseTresholdsBottomSheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.pawlowski.butanmonitor.ui.components.bottomSheet.BaseBottomSheet

@Composable
fun ChooseThresholdsBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (Int?, Int?) -> Unit,
) {
    BaseBottomSheet(show = show, onDismiss = onDismiss) {
        Column(
            verticalArrangement = Arrangement.spacedBy(space = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(bottom = 15.dp)
                    .padding(horizontal = 16.dp),
        ) {
            Text(
                text = "Ustaw progi do alertu",
                style = MaterialTheme.typography.titleMedium,
            )

            val ammoniaThreshold =
                remember {
                    mutableStateOf("")
                }
            TextField(
                value = ammoniaThreshold.value,
                label = {
                    Text(text = "Próg amoniaku")
                },
                onValueChange = {
                    if (it.isEmpty() || it.toIntOrNull() != null) {
                        ammoniaThreshold.value = it
                    }
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                modifier = Modifier.fillMaxWidth(),
            )

            val propaneThreshold =
                remember {
                    mutableStateOf("")
                }
            TextField(
                value = propaneThreshold.value,
                label = {
                    Text(text = "Próg propanu")
                },
                onValueChange = {
                    if (it.isEmpty() || it.toIntOrNull() != null) {
                        propaneThreshold.value = it
                    }
                },
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                    ),
                modifier = Modifier.fillMaxWidth(),
            )

            Button(
                onClick = {
                    val ammoniaThresholdValue = ammoniaThreshold.value.toIntOrNull()
                    val propaneThresholdValue = propaneThreshold.value.toIntOrNull()

                    hideBottomSheetWithAction {
                        onConfirm(
                            ammoniaThresholdValue,
                            propaneThresholdValue,
                        )
                    }
                },
                shape = RectangleShape,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF355CA8),
                        contentColor = Color.White,
                    ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Dodaj")
            }

            Button(
                onClick = {
                    dismissBottomSheet()
                },
                shape = RectangleShape,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF15C5C),
                        contentColor = Color.White,
                    ),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Anuluj")
            }
        }
    }
}
