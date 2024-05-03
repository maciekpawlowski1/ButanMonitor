package com.pawlowski.butanmonitor.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.pawlowski.butanmonitor.domain.model.Measurement
import com.pawlowski.butanmonitor.ui.components.chartNew.ChartNew
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import kotlinx.datetime.format.char

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

fun LocalDate.formatDate() =
    format(
        LocalDate.Format {
            dayOfMonth()
            char('.')
            monthNumber()
            char('.')
            year()
        },
    )

fun LocalTime.formatTime() =
    format(
        LocalTime.Format {
            hour()
            char(':')
            minute()
        },
    )

fun LocalDateTime.formatDateTime() =
    format(
        LocalDateTime.Format {
            dayOfMonth()
            char('.')
            monthNumber()
            char('.')
            year()
            char(' ')
            hour()
            char(':')
            minute()
        },
    )
