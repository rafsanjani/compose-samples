package com.example.composesamples.components.geometry.state

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Offset
import com.example.composesamples.components.geometry.shapes.Shape

@Stable
interface ShapeState {
    var initialShape: Shape
    fun onDrag(distance: Offset)
    fun onDragStarted(position: Offset)
    fun onDragEnd()
}