package com.example.composesamples.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding

@Composable
fun HoistedStateObject() {
    ProvideWindowInsets {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .systemBarsPadding()
                .fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Email(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    state = rememberEmailState(initialValue = "coded_raf@yahoo.com")
                )
            }
        }
    }
}

@Composable
fun Email(state: EmailState, modifier: Modifier = Modifier) {
    OutlinedTextField(
        modifier = modifier,
        value = state.email,
        onValueChange = {
            state.email = it
        },
        placeholder = {
            Text(text = "Enter email address here!")
        },
        colors = TextFieldDefaults.textFieldColors(),
        label = {
            Text(text = "Email Address")
        },
        isError = !state.isValid,
    )
}

@Composable
fun rememberEmailState(initialValue: String = "kwabena@gmail.com") =
    rememberSaveable(saver = EmailStateImpl.Saver) { EmailStateImpl(initialValue) }

@Stable
interface EmailState {
    var email: String
    var isValid: Boolean
}


private class EmailStateImpl(initialValue: String) : EmailState {
    private var _email by mutableStateOf(initialValue, structuralEqualityPolicy())
    private var _isValid by mutableStateOf(initialValue.contains("@"), structuralEqualityPolicy())

    override var email: String
        get() = _email
        set(value) {
            _email = value
        }

    override var isValid: Boolean
        get() = _email.contains("@")
        set(value) {
            _isValid = value
        }

    companion object {
        val Saver: Saver<EmailState, *> = listSaver(
            save = { listOf(it.email, it.isValid) },
            restore = {
                EmailStateImpl(
                    initialValue = it[0].toString()
                )
            }
        )
    }
}