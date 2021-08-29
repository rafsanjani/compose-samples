package com.example.composesamples.components.geometry.shapes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path

interface Shape {
    /**
     * The rectangle within which the shape is bounded
     */
    val rect: Rect

    /**
     * The actual path of the shape
     */
    val path: Path


    /**
     * Center coordinates of the shape. This should be equal to the absolute center of the rectangle within
     * which the shape is bounded.
     *
     * For a Line whose rect cannot be computed as a result of having a hairline width,
     * this will be equal to the difference between the end and start coordinates divided by 2
     */
    val center: Offset

}
