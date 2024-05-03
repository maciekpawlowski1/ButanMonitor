package com.pawlowski.butanmonitor.ui.screen.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pawlowski.butanmonitor.ui.components.chartNew.ChartNew
import com.pawlowski.butanmonitor.ui.components.chipWithDropdown.SettingsIconWithDropdown
import com.pawlowski.butanmonitor.ui.components.error.ErrorItem
import com.pawlowski.butanmonitor.ui.utils.rememberChartNewRecords
import com.pawlowski.butanmonitor.utils.Resource
import com.pawlowski.network.domain.Measurement
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    measurements: Resource<ImmutableList<Measurement>>,
    onBackClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TopAppBar(
            title = { Text(text = "Historyczne pomiary") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
        )

        val chosenTimestampDensity =
            remember {
                mutableStateOf(1.minutes)
            }

        SettingsIconWithDropdown { onDismiss ->
            DropdownMenuItem(
                text = { Text("1 minuta") },
                onClick = {
                    chosenTimestampDensity.value = 1.minutes
                    onDismiss()
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Timer,
                        contentDescription = null,
                    )
                },
            )
            DropdownMenuItem(
                text = { Text("10 minut") },
                onClick = {
                    chosenTimestampDensity.value = 10.minutes
                    onDismiss()
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Timer,
                        contentDescription = null,
                    )
                },
            )
            DropdownMenuItem(
                text = { Text("30 minut") },
                onClick = {
                    chosenTimestampDensity.value = 30.minutes
                    onDismiss()
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Timer,
                        contentDescription = null,
                    )
                },
            )
            DropdownMenuItem(
                text = { Text("1 godzina") },
                onClick = {
                    chosenTimestampDensity.value = 1.hours
                    onDismiss()
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Timer,
                        contentDescription = null,
                    )
                },
            )
            DropdownMenuItem(
                text = { Text("6 godzin") },
                onClick = {
                    chosenTimestampDensity.value = 6.hours
                    onDismiss()
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Timer,
                        contentDescription = null,
                    )
                },
            )
        }

        when (measurements) {
            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Error -> {
                ErrorItem(
                    onRetryClick = onRetryClick,
                )
            }
            is Resource.Success -> {
                val (propaneRecords, ammoniaRecords) = rememberChartNewRecords(measurements = measurements.data)

                ChartNew(
                    axisses =
                        persistentListOf(
                            ChartNew.Axis(
                                ammoniaRecords,
                                color = Color.Yellow,
                            ),
                            ChartNew.Axis(
                                propaneRecords,
                                color = Color.Blue,
                            ),
                        ),
                    widthConfig =
                        ChartNew.WidthConfig.Scrollable(
                            autoScroll = false,
                            timePerWidth = chosenTimestampDensity.value,
                        ),
                )
            }
        }
    }
}
