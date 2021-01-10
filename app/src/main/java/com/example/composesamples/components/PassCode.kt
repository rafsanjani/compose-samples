package com.example.composesamples.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composesamples.R
import com.example.composesamples.R.drawable.ic_backspace
import com.example.composesamples.ui.typography

//special keys

//the empty box on the bottom left
const val SPECIAL_KEY_EMPTY = 10
const val SPECIAL_KEY_ZERO = 11
const val SPECIAL_KEY_BACKSPACE = 12

private const val TAG = "PassCode"

@ExperimentalAnimationApi
@Composable
fun PassCode(
    correctPasscode: String = "123456",
    headerText: String = "Please enter your passcode!",
    onPasscodeAccepted: () -> Unit = {},
    onPasscodeRejected: () -> Unit = {},
    circleFillColor: Color = Color.Black,
    circlePadding: Dp = 5.dp,
    borderColor: Color = Color.Black,
    borderStrokeWidth: Dp = 2.dp,
    circleSize: Dp = 40.dp,
    backgroundColor: Color = Color.Transparent,
    backgroundImage: ImageBitmap? = imageResource(id = R.drawable.bg_passcode)
) {
    var typedPasscode by remember { mutableStateOf("") }
    var passCodeState by remember { mutableStateOf(PassCodeState.Undefined) }
    var inputAttempts by remember { mutableStateOf(0) }


    val transition = transition(
        onStateChangeFinished = {
            passCodeState = PassCodeState.Undefined
            typedPasscode = ""
        },
        definition = definition,
        toState = passCodeState
    )


    Box(
        modifier = Modifier
            .background(color = backgroundColor)
            .fillMaxSize()
    ) {

        backgroundImage?.let {
            Image(
                bitmap = backgroundImage,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }

        Column(
            modifier = Modifier
                .wrapContentSize()
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 70.dp)
                    .fillMaxWidth()
                    .align(Alignment.End),
                text = headerText,
                style = typography.h5
            )


            //passcode dots
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = transition[translation]
                    }
                    .padding(top = 50.dp)
                    .fillMaxWidth()
            ) {
                repeat(correctPasscode.length) {
                    PassCodeCircle(
                        circleFillColor = circleFillColor,
                        circlePadding = circlePadding,
                        borderColor = borderColor,
                        borderStrokeWidth = borderStrokeWidth,
                        circleSize = circleSize,
                        isFilled = it < typedPasscode.length
                    )
                }
            }


            if (inputAttempts > 0) {
                FeedbackText(
                    message = "Incorrect password entered!",
                    color = Color.Red
                )
            }

            if (passCodeState == PassCodeState.Accepted) {
                FeedbackText(message = "Passcode Accepted!", color = Color.DarkGray)
            }

            Box(modifier = Modifier.weight(1f)) {
                NumericalPad(
                    onClick = {
                        if (typedPasscode.length + 1 <= correctPasscode.length)
                            typedPasscode += it.toString()

                        if (typedPasscode.length == correctPasscode.length) {
                            if (typedPasscode == correctPasscode) {
                                passCodeState = PassCodeState.Accepted
                                inputAttempts = 0
                                onPasscodeAccepted()
                            } else {
                                passCodeState = PassCodeState.Wrong
                                inputAttempts++
                                onPasscodeRejected()
                            }
                        }
                    },
                    onDelete = {
                        typedPasscode = typedPasscode.dropLast(1)
                    })
            }
        }
    }
}

@Composable
fun FeedbackText(message: String, color: Color) {
    val alpha = animatedFloat(initVal = 0f)
    alpha.animateTo(1f, anim = tween(durationMillis = 500, easing = LinearEasing))

    Text(
        text = message,
        modifier = Modifier
            .alpha(alpha.value)
            .padding(top = 40.dp)
            .fillMaxWidth(),
        style = TextStyle(
            textAlign = TextAlign.Center,
            color = color,
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
fun PassCodeCircle(
    modifier: Modifier = Modifier,
    isFilled: Boolean,
    circleFillColor: Color,
    circlePadding: Dp,
    borderColor: Color,
    borderStrokeWidth: Dp,
    circleSize: Dp
) {
    Box(
        modifier = modifier
            .padding(circlePadding)
            .background(
                color = if (isFilled) circleFillColor else Color.Transparent,
                shape = CircleShape
            )
            .border(border = BorderStroke(borderStrokeWidth, borderColor), shape = CircleShape)
            .preferredSize(circleSize)
    )
}

@Composable
fun NumericalPad(onClick: (number: Int) -> Unit, onDelete: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .padding(top = 20.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        val numberPadArrangement = Arrangement.spacedBy(5.dp)

        Column(verticalArrangement = numberPadArrangement) {
            for (i in 1 until 5) {
                Row(
                    horizontalArrangement = numberPadArrangement,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (j in 1 until 4) {

                        val position = when (i) {
                            2 -> j + 3 //row 2
                            3 -> j + 6 // row 3
                            4 -> j + 9 // row 4
                            else -> j * i// row 1
                        }

                        NumericalKey(
                            onClick = {
                                when (it) {
                                    SPECIAL_KEY_EMPTY -> return@NumericalKey
                                    SPECIAL_KEY_BACKSPACE -> {
                                        onDelete()
                                    }
                                    SPECIAL_KEY_ZERO -> {
                                        onClick(0)
                                    }
                                    else -> {
                                        onClick(it)
                                    }
                                }
                            },
                            position = position,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun NumericalKey(position: Int, modifier: Modifier, onClick: (number: Int) -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .preferredHeight(70.dp)
            .clickable(
                indication = rememberRipple(color = Color.Cyan),
                onClick = {
                    onClick(position)
                })
            .background(
                brush = SolidColor(Color.White), shape = RoundedCornerShape(3.dp), alpha = 0.5f
            )
    ) {
        //delete key
        if (position == 12) {
            Icon(imageVector = vectorResource(id = ic_backspace))
        }

        if (position != SPECIAL_KEY_EMPTY && position != SPECIAL_KEY_BACKSPACE)
            Text(
                text = if (position == SPECIAL_KEY_ZERO) "0" else position.toString(),
                style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 25.sp)
            )

    }
}


private enum class PassCodeState {
    Undefined,
    Wrong,
    Accepted
}

private val translation = FloatPropKey(label = "dotsTranslation")

private val definition: TransitionDefinition<PassCodeState> =
    transitionDefinition {
        state(PassCodeState.Undefined) {
            this[translation] = 0f
        }

        state(PassCodeState.Wrong) {
            this[translation] = 30f
        }

        transition(PassCodeState.Undefined to PassCodeState.Wrong) {
            translation using repeatable(
                repeatMode = RepeatMode.Reverse,
                iterations = 10,
                animation = tween(
                    easing = LinearEasing,
                    durationMillis = 70
                )
            )
        }

        transition(PassCodeState.Wrong to PassCodeState.Undefined) {
            translation using tween(
                easing = LinearEasing,
                durationMillis = 100
            )
        }
    }


@ExperimentalAnimationApi
@Composable
@Preview
fun PasscodePreview() {
    PassCode()
}