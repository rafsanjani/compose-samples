package com.example.composesamples

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composesamples.components.NavHost
import com.example.composesamples.components.navigation.Destination
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
            ProvideWindowInsets() {
                NavHost(
                    modifier = Modifier
                        .systemBarsPadding()
                        .fillMaxSize()
                )
            }
        }
    }
}

// All screen samples excluding the main menu
val samples = listOf(
    Destination.Clock,
    Destination.Passcode,
    Destination.Menu,
    Destination.PaginatedList,
    Destination.LaunchedEffect,
    Destination.Draggable,
    Destination.HoistedStateObject,
    Destination.RememberUpdatedState,
    Destination.FreeFlowingRectangle,
    Destination.FreeFlowingCircle,
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainMenu(
    onNavigate: (destination: Destination) -> Unit = {}
) {
    ProvideWindowInsets {
        LazyColumn(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxSize()
        ) {
            val filteredSamples = samples
                .filter { it.route != Destination.Menu.route }

            items(items = filteredSamples, key = { it.route }) { destination ->
                DestinationCard(destination = destination, onNavigate = { onNavigate(it) })
            }
        }
    }
}

@Composable
fun DestinationCard(
    destination: Destination,
    modifier: Modifier = Modifier,
    onNavigate: (destination: Destination) -> Unit
) {
    val padding = 15.dp
    Column(
        modifier = modifier
            .clickable {
                onNavigate(destination)
            }
            .fillMaxWidth()
            .padding(start = padding, end = padding, top = padding),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = destination.route,
            style = MaterialTheme.typography.h6
        )

        Text(
            text = destination.description,
            style = MaterialTheme.typography.subtitle2
        )

        Divider(modifier = Modifier.fillMaxWidth())
    }
}

@Preview(showSystemUi = true)
@Composable
fun MainMenuPreview() {
    MainMenu()
}