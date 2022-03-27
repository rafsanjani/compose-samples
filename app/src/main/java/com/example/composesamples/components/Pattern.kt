package com.example.composesamples.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Pattern() {
    val patternSize = 60f
    var dots by remember { mutableStateOf(listOf<Dot>()) }

    var lineEnd by remember { mutableStateOf(Offset.Infinite) }

    val dotPairs = remember {
        mutableStateListOf<Pair<Dot, Dot>>()
    }

    var startDot by remember { mutableStateOf<Dot?>(null) }

    MaterialTheme {
        BoxWithConstraints(
            modifier = Modifier
                .background(Color.Green)
                .fillMaxSize()
                .padding(50.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { position ->
                            startDot = dots.find { it.contains(position) }
                            lineEnd = position
                        },
                        onDrag = { _, dragAmount ->
                            lineEnd += dragAmount

                            val endDot =
                                dots.find { it.contains(lineEnd) && it.position != startDot?.position }

                            if (endDot != null) {
                                if (startDot != null) {
                                    dotPairs.add(Pair(startDot!!, endDot))
                                    startDot = endDot
                                }
                            }
                        },
                        onDragEnd = {
                            dots = dots.map { it.copy(color = Color.Black) }
                            val chars = dotPairs.map { (a, b) ->
                                "${a.code}${b.code}"
                            }

                            println(chars.joinToString(separator = ""))

                            dotPairs.clear()
                            lineEnd = Offset.Infinite
                        },
                        onDragCancel = {
                            lineEnd = Offset.Infinite
                            dots = dots.map { it.copy(color = Color.Black) }
                            val chars = dotPairs.map { (a, b) ->
                                "${a.code}${b.code}"
                            }

                            println(chars.joinToString())
                            dotPairs.clear()
                        },
                    )
                }
        ) {

            val dotRadii = dots.map {
                animateFloatAsState(if (it.contains(lineEnd)) patternSize * 2 else patternSize)
            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                dots = generateGridPositions(radius = patternSize, rows = 3, cols = 3)

                dots.forEachIndexed { index, dot ->
                    patternDot(
                        dot.position,
                        dotRadii.getOrNull(index)?.value ?: patternSize,
                        color = dot.color
                    )
                }

                dotPairs.forEach { (s, e) ->
                    drawLine(
                        start = s.position,
                        end = e.position,
                        color = Color.Black,
                        strokeWidth = 40f
                    )
                }
            }
        }
    }
}

data class Dot(
    val position: Offset,
    val color: Color = Color.Black,
    val code: String,
    val radius: Float,
) {
    val rect: Rect get() = Rect(center = position, radius = radius)

    fun contains(offset: Offset): Boolean {
        return rect.contains(offset)
    }
}

/**
 * @param radius total size of the dot
 * @param rows total number of rows
 * @param cols total number of columns
 */
fun DrawScope.generateGridPositions(
    radius: Float,
    rows: Int,
    cols: Int
): List<Dot> {
    val dots = mutableListOf<Dot>()

    val fraction = 1 / (rows - 1f)

    var charCode = 'A'


    for (i in 0 until rows) {
        val x = lerp(0f, size.width, i * fraction)

        for (j in 0 until cols) {
            val y = lerp(0f, size.height, j * fraction)
            dots.add(
                Dot(
                    position = Offset(x = x, y = y),
                    radius = radius,
                    code = "$charCode$j"
                )
            )
        }
        charCode++
    }

    return dots
}


fun lerp(a: Float, b: Float, f: Float): Float {
    return (a * (1.0 - f) + b * f).toFloat()
}

fun DrawScope.patternDot(position: Offset, radius: Float, color: Color) {
    drawCircle(
        color = color,
        center = position,
        radius = radius
    )
}

@Preview
@Composable
fun PatternPreview() {
    Pattern()
}