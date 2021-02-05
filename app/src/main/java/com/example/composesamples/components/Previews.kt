package com.example.composesamples.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.insets.ExperimentalAnimatedInsets
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets

@ExperimentalFoundationApi
@Composable
@Preview
fun NewsSeparatorPreview() {
    Box(
        modifier = Modifier
            .background(color = Color.Yellow)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        NewsSeparator(
            date = "January 20, 2021",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
@Preview
fun ClockPreview() {
    Clock()
}

@ExperimentalAnimationApi
@Composable
@Preview
fun PassCodePreview() {
    PassCode()
}

@ExperimentalAnimatedInsets
@ExperimentalFoundationApi
@Preview
@Composable
fun NewsListPreview() {
    ProvideWindowInsets {
        PaginatedNews()
    }
}