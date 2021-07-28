package com.example.composesamples

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composesamples.components.Clock
import com.example.composesamples.components.PassCode
import com.example.composesamples.components.PaginatedList
import com.example.composesamples.styles.ComposeSamplesTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import java.util.*

@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun App() {
    ComposeSamplesTheme {
        val navController = rememberNavController()

        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(navController, startDestination = "menu") {
                composable("clock") { Clock() }
                composable("menu") {
                    MainMenu(
                        onNavigate = { destination ->
                            navController.navigate(destination)
                        }
                    )
                }
                composable("passcode") { PassCode() }
                composable("news") { PaginatedList() }
            }
        }
    }
}

@Composable
fun MainMenu(
    onNavigate: (destination: String) -> Unit = {}
) {
    val samples = listOf(
        "Clock",
        "Passcode",
        "News",
    )

    ProvideWindowInsets {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxSize()
        ) {
            samples.forEach { name ->
                Text(
                    fontWeight = FontWeight.ExtraBold,
                    text = name, modifier = Modifier
                        .clickable {
                            onNavigate(name.toLowerCase(Locale.getDefault()))
                        }
                        .padding(15.dp)
                        .fillMaxWidth()
                )
            }
        }
    }

}


@Preview(showSystemUi = true)
@Composable
fun MainMenuPreview() {
    MainMenu()
}