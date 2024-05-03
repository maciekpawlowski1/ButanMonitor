package com.pawlowski.butanmonitor.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.pawlowski.butanmonitor.domain.model.Measurement
import com.pawlowski.butanmonitor.ui.components.chartNew.ChartNew
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun rememberChartNewRecords(
    measurements: ImmutableList<Measurement>,
): Pair<ImmutableList<ChartNew.Record>, ImmutableList<ChartNew.Record>> {
    val propaneRecords =
        remember(measurements) {
            measurements.map {
                ChartNew.Record(
                    timestamp = it.timestamp,
                    value = it.propaneLevel,
                )
            }.toPersistentList()
        }
    val ammoniaRecords =
        remember(measurements) {
            measurements.map {
                ChartNew.Record(
                    timestamp = it.timestamp,
                    value = it.ammoniaLevel,
                )
            }.toPersistentList()
        }

    return propaneRecords to ammoniaRecords
}
