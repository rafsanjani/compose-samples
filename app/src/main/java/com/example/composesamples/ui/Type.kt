package com.example.composesamples.ui

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.composesamples.R

val fontFamily = fontFamily(
    font(
        resId = R.font.patua_one,
        weight = FontWeight.Bold,
        style = FontStyle.Normal
    )
)


// Set of Material typography styles to start with
val typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h5 = TextStyle(
        textAlign = TextAlign.Center,
        fontFamily = fontFamily,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold
    )
)


