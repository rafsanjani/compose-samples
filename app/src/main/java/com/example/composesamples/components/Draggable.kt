package com.example.composesamples.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun Draggable() {
    var ballPosition by remember { mutableStateOf(IntOffset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        Ball(position = ballPosition, onPositionChanged = { ballPosition += it })
    }

}


@Composable
private fun Ball(position: IntOffset, onPositionChanged: (change: IntOffset) -> Unit) {
    Box(
        modifier = Modifier
            .offset { position }
            .size(80.dp)
            .background(color = Color.Yellow, shape = CircleShape)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    onPositionChanged(IntOffset(dragAmount.x.toInt(), dragAmount.y.toInt()))
                    change.consumeAllChanges()
                }
            }
    )
}