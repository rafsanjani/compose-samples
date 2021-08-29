import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.delay

@Composable
//Special credits to everyone in this slack thread:
fun RememberUpdatedStateSample() {
    var counter by remember {
        mutableStateOf(0)
    }

    val displayText = counter.toString()

    val childLambda = {
        displayText
    }

    LaunchedEffect(Unit) {
        //After the first iteration, displayText becomes stale as childLambda still retains the
        //original displayText value it was created with
        while (true) {
            delay(250)
            counter++
        }
    }


    val snackbarHostState = SnackbarHostState()

    Scaffold(
        scaffoldState = rememberScaffoldState(
            snackbarHostState = snackbarHostState
        )
    ) {
        Column(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(150.dp),
                text = "Counting Forever: $counter",
                textAlign = TextAlign.Center
            )

            SomeChildComposable(
                callback = childLambda, snackbarHostState
            )
        }
    }
}

@Composable
fun SomeChildComposable(
    callback: () -> String,
    snackbarHostState: SnackbarHostState
) {
    //rememberUpdatedState ensures that the callback will always have the latest version of its displayText value
    val currentTextValue by rememberUpdatedState(newValue = callback)

    LaunchedEffect(true) {
        delay(5000)

        snackbarHostState.showSnackbar(
            //calling callback() here will return 0 because it was originally created with displayText value of 0
            message = "Current counter value: ${currentTextValue()}"
        )
    }

}