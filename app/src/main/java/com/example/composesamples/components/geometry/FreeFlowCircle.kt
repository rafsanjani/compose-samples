package com.example.composesamples.components.geometry

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.composesamples.components.geometry.shapes.Circle
import com.example.composesamples.components.geometry.state.CircleState
import com.example.composesamples.components.geometry.state.rememberCircleState

@Composable
fun FreeFlowCircle() {
    val shape by remember { mutableStateOf(Circle.Default) }

    Box(modifier = Modifier.fillMaxSize()) {
        FreeFlowCircle(
            state = rememberCircleState(
                initialShape = shape,
            )
        )
        Text(
            text = "Drag the yellow or green dot to apply transformations",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(30.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun FreeFlowCircle(state: CircleState) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        state.onDragStarted(it)
                    },
                    onDrag = { change, dragAmount ->
                        state.onDrag(dragAmount)
                        change.consumeAllChanges()
                    },
                    onDragEnd = {
                        state.onDragEnd()
                    }
                )
            },
        onDraw = {
            drawPath(
                path = state.shape.path,
                color = Color.Black,
                style = Stroke(5f)
            )

            //draw resize handle
            drawCircle(
                center = state.resizeHandle.center,
                brush = SolidColor(Color.Green),
                alpha = 0.5f,
                radius = state.resizeHandleSize
            )

            //draw movement circle
            drawCircle(
                center = state.shape.center,
                brush = SolidColor(Color.Yellow),
                alpha = 0.9f,
                radius = state.moveHandleSize
            )
        }
    )
}


