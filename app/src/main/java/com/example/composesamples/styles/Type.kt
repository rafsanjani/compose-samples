package com.example.composesamples.styles

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.composesamples.R

private val patua = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.patua_one,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        )
    )
)

private val museo = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.museo_slab
        ),
        Font(
            resId = R.font.museo_slab,
            weight = FontWeight.Normal,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.museo_slab,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        ),
    )
)


// Set of Material typography styles to start with
val PasscodeTypography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h5 = TextStyle(
        textAlign = TextAlign.Center,
        fontFamily = patua,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold
    )
)
val NewsTypography = Typography(
    body1 = TextStyle(
        fontFamily = museo,
        fontSize = 16.sp
    ),

    h6 = TextStyle(
        fontFamily = museo,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    ),
)

