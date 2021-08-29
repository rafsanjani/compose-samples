package com.example.composesamples.components.geometry.state

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.example.composesamples.components.geometry.ResizeDirection
import com.example.composesamples.components.geometry.shapes.Rectangle
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class RectangleState(
    initialShape: Rectangle
) : AbstractShapeState(initialShape) {


    var rotationAngle by mutableStateOf(120.0)

    override val resizeHandleSize: Float
        get() = 50f

    override val resizeHandle: Rect
        get() {
            val x =
                (shape.center.x + cos(Math.toRadians(rotationAngle)) * shape.rect.size.width / 2).toFloat()
            val y =
                (shape.center.y + sin(Math.toRadians(rotationAngle)) * shape.rect.size.width / 2).toFloat()
            return Rect(center = Offset(x, y), resizeHandleSize)
        }

    private var currentPosition by mutableStateOf(resizeHandle.center)

    override val moveHandle: Rect
        get() = Rect(center = shape.center, radius = moveHandleSize)

    override val shape: Rectangle
        get() = super.shape as Rectangle

    override fun onDrag(distance: Offset) {
        currentPosition += distance

        if (canResize) {
            rotationAngle = getRotationAngle(currentPosition)
            Log.d("Rafs", "onDrag: $rotationAngle")
        }

        if (canMove) {
            val oldShape = (shapeData as Rectangle)
            shapeData = oldShape.copy(
                topLeft = oldShape.topLeft + distance,
                bottomRight = oldShape.bottomRight + distance
            )
        }
    }

    private fun getResizeHandleCoord(): Offset {
        val x =
            (shape.center.x + cos(Math.toRadians(rotationAngle)) * shape.rect.size.width / 2).toFloat()
        val y =
            (shape.center.y + sin(Math.toRadians(rotationAngle)) * shape.rect.size.width / 2).toFloat()

        return Offset(x, y)
    }

    private fun getRotationAngle(target: Offset): Double {
        val (dx, dy) = target - shape.center
        val theta = atan2(dy, dx).toDouble()

        var angle = Math.toDegrees(theta)

        if (angle < 0) {
            angle += 360.0
        }
        return angle
    }
}

// TODO: Use a rememberSaveable
@Composable
fun rememberRectangleState(
    initialShape: Rectangle,
    resizeDirection: ResizeDirection = ResizeDirection.BottomRight
) = remember {
    RectangleState(initialShape)
}
