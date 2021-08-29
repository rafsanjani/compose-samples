package com.example.composesamples.components.geometry.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.example.composesamples.components.geometry.shapes.Circle
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

class CircleState(
    initialShape: Circle,
) : AbstractShapeState(initialShape) {

    var rotationAngle by mutableStateOf(0.0)

    //track the center of the resizeHandle for movements
    private var currentPosition by mutableStateOf(resizeHandle.center)

    override val resizeHandle: Rect
        get() {
            val x = (shape.center.x + cos(Math.toRadians(rotationAngle)) * shape.radius).toFloat()
            val y = (shape.center.y + sin(Math.toRadians(rotationAngle)) * shape.radius).toFloat()

            return Rect(center = Offset(x, y), resizeHandleSize)
        }
    override val moveHandle: Rect
        get() = Rect(shape.center, moveHandleSize)


    override val shape: Circle
        get() = super.shape as Circle

    override fun onDrag(distance: Offset) {
        currentPosition += distance

        if (canResize) {
            val (dx, dy) = resizeHandle.center - (shape.center - distance)

            shapeData = shape.copy(
                radius = hypot(dx, dy)
            )

            rotationAngle = getRotationAngle(currentPosition)
        }

        if (canMove) {
            shapeData = shape.copy(
                center = shape.center + distance
            )
        }
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

// TODO: use a rememberSaveable
@Composable
fun rememberCircleState(
    initialShape: Circle,
) = remember {
    CircleState(initialShape)
}
