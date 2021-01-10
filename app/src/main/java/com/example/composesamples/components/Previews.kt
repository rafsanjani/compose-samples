package com.example.composesamples.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


private const val TAG = "Previews"

enum class State {
    LEFT,
    MIDDLE,
    RIGHT
}

@ExperimentalAnimationApi
@Preview
@Composable
fun Preview() {
    PassCode()
}
