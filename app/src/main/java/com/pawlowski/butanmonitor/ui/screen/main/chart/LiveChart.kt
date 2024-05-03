package com.pawlowski.butanmonitor.ui.screen.main.chart

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pawlowski.butanmonitor.domain.model.Measurement
import com.pawlowski.butanmonitor.ui.components.chartNew.ChartNew
import com.pawlowski.butanmonitor.ui.utils.rememberChartNewRecords
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun LiveChart(measurements: ImmutableList<Measurement>) {
    Column {
        val isAutoScrolling =
            remember {
                mutableStateOf(true)
            }
        FilterChip(
            selected = isAutoScrolling.value,
            onClick = { isAutoScrolling.value = !isAutoScrolling.value },
            label = { Text(text = "Śledzenie") },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )

        val (propaneRecords, ammoniaRecords) = rememberChartNewRecords(measurements = measurements)
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
                    autoScroll = isAutoScrolling.value,
                    secondsPerScreenWidth = 60000L,
                ),
        )
    }
}
