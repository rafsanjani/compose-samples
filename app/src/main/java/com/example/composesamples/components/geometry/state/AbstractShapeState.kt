package com.example.composesamples.components.geometry.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.example.composesamples.components.geometry.shapes.Shape

abstract class AbstractShapeState(
    final override var initialShape: Shape
) : ShapeState {
    protected var shapeData: Shape by mutableStateOf(initialShape)
    protected var canMove by mutableStateOf(false)
    protected var canResize by mutableStateOf(false)
    open val resizeHandleSize = 70f
    open val moveHandleSize = 70f

    abstract val moveHandle: Rect

    open val shape: Shape get() = shapeData


    abstract val resizeHandle: Rect

    override fun onDragStarted(position: Offset) {
        canResize = resizeHandle.contains(position)
        canMove = Rect(shape.center, moveHandleSize).contains(position)
    }

    override fun onDragEnd() {
        canResize = false
        canMove = false
    }
}