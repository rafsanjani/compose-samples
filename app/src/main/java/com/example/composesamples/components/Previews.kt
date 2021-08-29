package com.example.composesamples.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.composesamples.components.sideeffects.LaunchedEffectSample
import com.google.accompanist.insets.ProvideWindowInsets


@ExperimentalFoundationApi
@Composable
@Preview
fun NewsSeparatorPreview() {
    NewsSeparator(
        date = "January 20, 2021",
    )
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

@ExperimentalFoundationApi
@Preview
@Composable
fun NewsListPreview() {
    ProvideWindowInsets {
        PaginatedList()
    }
}

@Preview
@Composable
fun AppPreview(){
    LaunchedEffectSample()
}