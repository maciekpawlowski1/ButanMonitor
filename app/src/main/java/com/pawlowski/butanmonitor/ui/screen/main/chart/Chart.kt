package com.pawlowski.butanmonitor.ui.screen.main.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import com.pawlowski.butanmonitor.domain.model.Measurement
import kotlinx.coroutines.delay
import kotlin.math.abs

@Composable
fun ChartContent(measurements: List<Measurement>) {
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

        val propaneRecords =
            remember(measurements) {
                measurements.map {
                    Record(
                        timestamp = it.timestamp,
                        value = it.propaneLevel,
                    )
                }
            }
        val ammoniaRecords =
            remember(measurements) {
                measurements.map {
                    Record(
                        timestamp = it.timestamp,
                        value = it.ammoniaLevel,
                    )
                }
            }

        val scrollOffset =
            remember {
                mutableFloatStateOf(0f)
            }

        val maxScrollAvailable =
            maxOf(
                propaneRecords.maxScrollAvailable(),
                ammoniaRecords.maxScrollAvailable(),
            )

        val scrollState =
            rememberScrollableState { delta ->
                scrollOffset.floatValue =
                    (scrollOffset.floatValue + delta)
                        .coerceAtLeast(minimumValue = -maxScrollAvailable)
                        .coerceAtMost(maximumValue = 0f)
                delta
            }

        LaunchedEffect(key1 = Unit) {
            while (true) {
                if (!scrollState.isScrollInProgress && isAutoScrolling.value) {
                    runCatching {
                        scrollState.scrollBy(-(abs(scrollOffset.floatValue) - maxScrollAvailable))
                    }
                }
                delay(10)
            }
        }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .scrollable(
                        state = scrollState,
                        orientation = Orientation.Horizontal,
                    ),
        ) {
            Chart(
                axisses =
                    listOf(
                        ChartAxis(
                            records = propaneRecords,
                            color = Color.Blue,
                        ),
                        ChartAxis(
                            records = ammoniaRecords,
                            color = Color.Yellow,
                        ),
                    ),
                translateOffset = scrollOffset::value,
            )
        }
    }
}

@Composable
private fun List<Record>.maxScrollAvailable(): Float {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val density = LocalDensity.current

    val minTimestamp = minOf { it.timestamp }
    val maxTimestamp = maxOf { it.timestamp }
    val diff = maxTimestamp - minTimestamp
    val scaleX = with(density) { screenWidth.toPx() } / WIDTH_TIMESTAMP

    return (diff * scaleX) - with(density) { screenWidth.toPx() }
}

const val WIDTH_TIMESTAMP = 60000

data class ChartAxis(
    val records: List<Record>,
    val color: Color,
)

@Composable
private fun Chart(
    axisses: List<ChartAxis>,
    translateOffset: () -> Float,
) {
    val minTimestamp =
        axisses.minOf {
            if (it.records.isNotEmpty()) {
                it.records.minOf { it.timestamp }
            } else {
                0
            }
        }
    val maxValue =
        axisses.maxOf {
            if (it.records.isNotEmpty()) {
                it.records.maxOf { it.value }.coerceAtLeast(minimumValue = 0) + 5
            } else {
                5
            }
        }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()

    val scaleX = with(density) { screenWidth.toPx() } / WIDTH_TIMESTAMP
    Canvas(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(300.dp),
    ) {
        val scaleY = size.height / maxValue

        val scaledRecords =
            axisses.map {
                it.records.map {
                    Offset(
                        x = (it.timestamp - minTimestamp) * scaleX,
                        y = (size.height - (it.value * scaleY)),
                    )
                } to it.color
            }

        drawHorizontalHelperLines(
            maxHelperValue = maxValue.toLong(),
            scaleY = scaleY,
            textMeasurer = textMeasurer,
        )

        translate(left = translateOffset()) {
            scaledRecords.forEach { (points, color) ->
                drawRecordsPath(
                    recordsPoints = points,
                    color = color,
                )
            }
        }
    }
}

private fun DrawScope.drawRecordsPath(
    recordsPoints: List<Offset>,
    color: Color,
) {
    val path =
        Path().apply {
            val firstPoint = recordsPoints.first()
            moveTo(
                x = firstPoint.x,
                y = firstPoint.y,
            )
            recordsPoints.drop(1).forEach { record ->
                lineTo(
                    x = record.x,
                    y = record.y,
                )
            }
        }

    drawPath(
        path = path,
        color = color,
        style =
            Stroke(
                width = 3.dp.toPx(),
            ),
    )
}

private fun DrawScope.drawHorizontalHelperLines(
    maxHelperValue: Long,
    scaleY: Float,
    textMeasurer: TextMeasurer,
) {
    val step = 10
    val linesCount = (maxHelperValue / step).toInt()
    repeat(linesCount) {
        val lineY = it * step
        val lineYScaled = lineY * scaleY
        val lineYSwapped = size.height - lineYScaled

        val textLayoutResult =
            textMeasurer.measure(
                text =
                    buildAnnotatedString {
                        append(lineY.toString())
                    },
            )
        drawText(
            textLayoutResult = textLayoutResult,
            topLeft =
                Offset(
                    x = 0f,
                    y = lineYSwapped - textLayoutResult.size.height,
                ),
        )

        drawLine(
            start =
                Offset(
                    x = 0f,
                    y = lineYSwapped,
                ),
            end =
                Offset(
                    x = size.width,
                    y = lineYSwapped,
                ),
            color = Color.Gray,
            strokeWidth = 1f,
            pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(10f, 5f)),
        )
    }
}
