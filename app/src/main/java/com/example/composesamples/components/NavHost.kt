package com.example.composesamples.components

import RememberUpdatedStateSample
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.composesamples.MainMenu
import com.example.composesamples.components.sideeffects.LaunchedEffectSample
import com.example.composesamples.samples
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun NavHost() {
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController,
        startDestination = Destination.Menu.route,
        modifier = Modifier.fillMaxSize()
    ) {
        samples.forEach { destination ->
            composable(
                destination = destination,
                navController = navController
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composable(
    destination: Destination,
    navController: NavController
) {

    val enterTransition =
        slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(400))

    val exitTransition =
        slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(400))

    val popExitTransition =
        slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(400))

    val popEnterTransition =
        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(400))

    composable(
        route = destination.route,
        enterTransition = { _, _ ->
            enterTransition
        },
        popEnterTransition = { _, _ ->
            popEnterTransition
        },
        popExitTransition = { _, _ ->
            popExitTransition
        },
        exitTransition = { _, _ ->
            exitTransition
        }
    ) {
        if (destination.route == Destination.Menu.route) {
            MainMenu { destination ->
                navController.navigate(destination.route)
            }
        } else {
            destination.content()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
sealed class Destination(val route: String, val content: @Composable () -> Unit = {}) {
    object Clock : Destination("Clock", content = { Clock() })
    object Passcode : Destination("Passcode", content = { PassCode() })
    object Drawing : Destination("Drawing", content = {})
    object Menu : Destination("Menu")
    object PaginatedList : Destination("Paginated List", content = { PaginatedList() })
    object LaunchedEffect : Destination("Launched Effect", content = { LaunchedEffectSample() })
    object Draggable : Destination("Draggable", content = { Draggable() })
    object HoistedStateObject :
        Destination("HoistedStateObject", content = { HoistedStateObject() })

    object RememberUpdatedState :
        Destination("Remember Updated State", content = { RememberUpdatedStateSample() })

    // TODO: Read images from gallery and display them with animations
}