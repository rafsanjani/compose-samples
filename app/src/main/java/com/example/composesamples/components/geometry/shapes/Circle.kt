package com.example.composesamples.components.geometry.shapes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

data class Circle(
    override val center: Offset,
    val radius: Float
) : Shape {
    override val path: Path = Path().apply {
        addArc(rect, 0f, 360f)
        close()
    }

    override val rect: Rect get() = Rect(center, radius = radius)

    companion object {
        val Default: Circle =
            Circle(
                center = Offset(400f, 250f),
                radius = 200f
            )
    }
}