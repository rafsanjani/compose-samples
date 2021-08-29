package com.example.composesamples.components.geometry.shapes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

data class Rectangle(
    val topLeft: Offset,
    val bottomRight: Offset
) : Shape {
    override val path: Path = Path().apply {
        addRect(rect)
        close()
    }

    override val rect: Rect get() = Rect(topLeft = topLeft, bottomRight = bottomRight)

    override val center: Offset
        get() = rect.center

    companion object {
        val Default: Rectangle =
            Rectangle(
                topLeft = Offset(100f, 100f),
                bottomRight = Offset.Zero + Offset(400f, 400f)
            )
    }
}