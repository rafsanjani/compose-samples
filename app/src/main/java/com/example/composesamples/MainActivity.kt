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
import com.example.composesamples.components.Destination
import com.example.composesamples.components.NavHost
import com.example.composesamples.styles.ComposeSamplesTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface {
                App()
            }
        }
    }
}

@Composable
fun App() {
    ComposeSamplesTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            NavHost()
        }
    }
}

// All screen samples excluding the main menu
val samples = listOf(
    Destination.Clock,
    Destination.Passcode,
    Destination.Menu,
    Destination.PaginatedList,
    Destination.Drawing,
    Destination.LaunchedEffect,
    Destination.Draggable,
    Destination.HoistedStateObject
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainMenu(
    onNavigate: (destination: Destination) -> Unit = {}
) {
    ProvideWindowInsets {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxSize()
        ) {
            samples
                .filter { it.route != Destination.Menu.route }
                .forEach { destination ->
                    Text(
                        fontWeight = FontWeight.ExtraBold,
                        text = destination.route,
                        modifier = Modifier
                            .clickable {
                                onNavigate(destination)
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