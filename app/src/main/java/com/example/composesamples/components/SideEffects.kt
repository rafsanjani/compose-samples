package com.example.composesamples.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun LaunchedEffectSample() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            var textValue by remember { mutableStateOf(0) }
            val launchedEffectToggle by rememberUpdatedState(mutableStateOf(true))

            LaunchedEffect(launchedEffectToggle.value) {
                textValue = 0
                while (textValue < 10) {
                    delay(1000)
                    textValue++
                }
                textValue = 0
            }

            Text(
                text = textValue.toString(),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Button(onClick = {
                launchedEffectToggle.value = !launchedEffectToggle.value
            }) {
                Text(text = "Start Effect")
            }
        }
    }
}

@Composable
fun DraggableSample() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var position by remember { mutableStateOf(IntOffset.Zero) }

        fun onPositionChange(change: IntOffset) {
            position += change
        }

        DraggableCircle(position = position, onPositionChanged = ::onPositionChange)
    }
}

@Composable
fun DraggableCircle(
    modifier: Modifier = Modifier,
    position: IntOffset,
    onPositionChanged: (change: IntOffset) -> Unit
) {
    Box(
        modifier = modifier
            .offset { position }
            .size(100.dp)
            .background(color = androidx.compose.ui.graphics.Color.Green, CircleShape)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    onPositionChanged(dragAmount.toIntOffset)
                    change.consumeAllChanges()
                }
            }
    )
}

val Offset.toIntOffset: IntOffset get() = IntOffset(x = x.roundToInt(), y = y.roundToInt())