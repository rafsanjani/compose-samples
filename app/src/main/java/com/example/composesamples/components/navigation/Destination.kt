package com.example.composesamples.components.navigation

import RememberUpdatedStateSample
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import com.example.composesamples.components.Clock
import com.example.composesamples.components.Draggable
import com.example.composesamples.components.HoistedStateObject
import com.example.composesamples.components.PaginatedList
import com.example.composesamples.components.PassCode
import com.example.composesamples.components.Pattern
import com.example.composesamples.components.gallery.GallerySample
import com.example.composesamples.components.geometry.FreeFlowCircle
import com.example.composesamples.components.geometry.FreeFlowRectangle
import com.example.composesamples.components.sideeffects.LaunchedEffectSample

@OptIn(ExperimentalFoundationApi::class)
sealed class Destination(
    val route: String,
    val description: String = "",
    val content: @Composable () -> Unit = {}
) {
    object Clock : Destination(
        route = "Clock",
        content = { Clock() },
        description = "Clock with infinite transition"
    )

    object Passcode : Destination(
        route = "Passcode",
        content = { PassCode() },
        description = "Passcode screen with update transitions"
    )

    object Menu : Destination("Menu")
    object PaginatedList : Destination(
        route = "Paginated List",
        content = { PaginatedList() },
        description = "Paginated List from a network resource"
    )

    object LaunchedEffect : Destination("Launched Effect", content = { LaunchedEffectSample() })
    object Draggable : Destination(
        route = "Draggable",
        content = { Draggable() },
        description = "Basic draggable ball with pointerInput"
    )

    object HoistedStateObject :
        Destination(
            route = "HoistedStateObject",
            content = { HoistedStateObject() },
            description = "Basic hoisted state object for email validation"
        )

    object RememberUpdatedState :
        Destination(
            route = "Remember Updated State",
            content = { RememberUpdatedStateSample() },
            description = "Remember updated state with lambda parameter"
        )

    object FreeFlowingRectangle :
        Destination(
            route = "Free Flowing Rectangle",
            content = { FreeFlowRectangle() },
            description = "A free flowing rectangle with advance hosted object techniques"
        )

    object FreeFlowingCircle :
        Destination(
            route = "Free Flowing Circle",
            content = { FreeFlowCircle() },
            description = "A free flowing circle with advance hoisted state object"
        )

    object Gallery :
        Destination(
            route = "Gallery",
            content = { GallerySample() },
            description = "Display images from the device gallery"
        )

    object Pattern :
        Destination(
            route = "Screen Lock Pattern",
            content = { Pattern() },
            description = "Screen lock pattern"
        )
}