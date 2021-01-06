package com.example.composesamples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.tooling.preview.Preview
import com.example.composesamples.components.Clock
import com.example.composesamples.ui.ComposeSamplesTheme


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    ComposeSamplesTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = Color.White) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Clock()
            }
        }

    }
}

@Preview
@Composable
fun DefaultPreview() {
    App()
}