package com.example.composesamples.components

import android.graphics.Rect
import android.graphics.Typeface
import android.text.TextPaint
import android.util.Log
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


@Composable
fun Clock() {
    val transition = rememberInfiniteTransition()

    val animatedValue by transition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec =
        infiniteRepeatable(
            animation = tween(1000)
        )
    )

    //Recomposition doesn't occur if we don't actually use the animated value within the composition
    //but what we really need is just an infinite loop
    Log.d("Debug", "Clock: $animatedValue")

    val time = LocalTime.now()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        val drawColor = MaterialTheme.colors.onSurface

        Canvas(
            modifier = Modifier
                .padding(top = 50.dp)
                .height(350.dp)
                .fillMaxWidth(),
            onDraw = {
                val radius = min(size.width / 2, size.height / 2) - 50

                //the shape's center
                val origin = Offset(size.width / 2, size.height / 2)

                val timeLabels = 1..12

                val paint = TextPaint().apply {
                    isAntiAlias = true
                    textSize = 35f
                    typeface = Typeface.DEFAULT_BOLD
                    this.color = drawColor.toArgb()
                }


                //draw letters
                val rect = Rect()
                for (i in timeLabels) {
                    val label = i.toString()

                    paint.getTextBounds(label, 0, label.length, rect)

                    val angle = Math.toRadians((i - 3) * 30.0).toFloat()

                    val x = (size.width / 2 + cos(angle) * (radius - 45) - rect.width() / 2)
                    val y = (size.height / 2 + sin(angle) * (radius - 45) + rect.height() / 2)

                    drawContext.canvas.nativeCanvas.drawText(label, x, y, paint)
                }

                //draw Hour tick
                drawTick(isHour = true, radius = radius, center = origin, color = drawColor)

                //draw minute tick
                drawTick(isHour = false, radius = radius, center = origin, color = drawColor)


                val millis = (time.get(ChronoField.MILLI_OF_SECOND)) / 1000f

                //draw second hand
                drawHand(
                    radius = radius,
                    value = millis + time.second,
                    from = origin,
                    distFromRadius = 50,
                    color = Color.Red,
                    strokeWidth = 2f
                )

                //draw minute hand
                drawHand(
                    radius = radius,
                    value = time.minute.toFloat(),
                    from = origin,
                    distFromRadius = 50,
                    color = drawColor,
                    strokeWidth = 5f
                )

                //draw hour hand
                drawHand(
                    isHour = true,
                    radius = radius,
                    value = time.hour.toFloat(),
                    from = origin,
                    distFromRadius = 100,
                    color = drawColor,
                    strokeWidth = 5f
                )

                //draw smaller inner circle
                drawCircle(
                    center = origin,
                    color = drawColor,
                    radius = 10f
                )

                //draw bigger outer circle
                drawCircle(
                    center = origin,
                    color = drawColor,
                    radius = radius,
                    style = Stroke(width = 4f)
                )
            })

        val timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a")

        Text(
            modifier = Modifier
                .border(
                    border = BorderStroke(width = 1.dp, color = drawColor),
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(8.dp),
            text = timeFormatter.format(time)
                .uppercase(Locale.getDefault()),
        )
    }

}

fun DrawScope.drawTick(isHour: Boolean, radius: Float, center: Offset, color: Color) {
    val ticks = if (isHour) 1..12 else 1..60

    for (i in ticks) {

        val tickLength = if (isHour) 20f else 15f

        val rotation = if (isHour) 30f * i else 6f * i

        if (!isHour && rotation % 30.0 == 0.0) continue

        rotate(rotation) {
            val tickPosX = (center.x + cos(Math.toRadians(90.0)) * radius).toFloat()
            val tickPosY = (center.y + sin(Math.toRadians(90.0)) * radius).toFloat()

            val pos = Offset(tickPosX, tickPosY)

            drawLine(
                start = pos,
                end = pos - Offset(x = 0f, y = tickLength),
                strokeWidth = if (isHour) 5f else 3f,
                cap = StrokeCap.Round,
                color = color
            )
        }
    }
}

fun DrawScope.drawHand(
    isHour: Boolean = false,
    radius: Float,
    value: Float,
    from: Offset,
    distFromRadius: Int,
    color: Color,
    strokeWidth: Float
) {
    //divide the circle into twelve segments for an hour hand and 60 for a minute.
    val divider = if (isHour) 12 else 60
    val angle = value / divider * 360
    val handRadius = radius - distFromRadius

    rotate(angle) {
        drawLine(
            start = from + Offset(x = 0f, y = 60f),
            end = from - Offset(x = 0f, y = handRadius),
            cap = StrokeCap.Round,
            strokeWidth = strokeWidth,
            color = color
        )
    }
}
