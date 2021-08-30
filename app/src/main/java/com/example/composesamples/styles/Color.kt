package com.example.composesamples.styles

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val purple200 = Color(0xFFBB86FC)
val purple500 = Color(0xFF6200EE)
val purple700 = Color(0xFF3700B3)
val teal200 = Color(0xFF03DAC5)
val darkGrey = Color(0xFF373737)
val lightGrey = Color(0xFFEEEEEE)


val Colors.cardBackground: Color
    @Composable
    get() = if (isLight) lightGrey else darkGrey

val Colors.separatorBackground: Color
    @Composable
    get() = if (isLight) Color(0xFFE0E0E0) else Color(0xFF494949)
