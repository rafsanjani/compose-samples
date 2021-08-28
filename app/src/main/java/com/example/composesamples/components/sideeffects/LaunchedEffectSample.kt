package com.example.composesamples.components.sideeffects

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LaunchedEffectSample() {
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    var counter by remember {
        mutableStateOf(0)
    }

    var effectToggle by remember { mutableStateOf(false) }

    LaunchedEffect(effectToggle) {
        counter = 0
        while (counter < 10) {
            counter++
            delay(1000)
        }

        snackbarHostState.showSnackbar(
            "Launched effect completed!"
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = rememberScaffoldState(
            snackbarHostState = snackbarHostState
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(text = counter.toString())

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(56.dp),
                onClick = {
                    effectToggle = !effectToggle
                }
            ) {
                Text(
                    text = "Restart Launched Effect",
                )
            }

            Text(
                text = "This will count to 10 only when effect toggle is changed by pressing the button",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}