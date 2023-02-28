package com.example.nano.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.nano.ui.utils.TextFieldState
import com.example.nano.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(
    label: String,
    state: TextFieldState,
    modifier: Modifier = Modifier,
    showErrorOnFirst: Boolean = false,
    imeAction: ImeAction = ImeAction.Done
) {
    val showPassword = rememberSaveable { mutableStateOf(false) }
    OutlinedTextField(
        value = state.text,
        onValueChange = {
            state.text = it
            if (showErrorOnFirst) state.enableShowErrors()
        },
        modifier = modifier
            .fillMaxWidth()
            .sizeIn(maxWidth = 300.dp)
            .onFocusChanged { focusState ->
                state.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    state.enableShowErrors()
                }
            },
        maxLines = 1,
        label = { Text(text = label) },
        trailingIcon = {
            if (state.isFocus) {
                if (showPassword.value) {
                    IconButton(onClick = { showPassword.value = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = stringResource(id = R.string.hide_password)
                        )
                    }
                } else {
                    IconButton(onClick = { showPassword.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = stringResource(id = R.string.show_password)
                        )
                    }
                }
            }
        },
        visualTransformation = if (showPassword.value) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation('\u2B50')
        },
        isError = state.isError(),
        supportingText = {
            state.getError()?.let { error -> TextFieldError(textError = error) }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Password
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextInput(
    label: String,
    state: TextFieldState,
    modifier: Modifier = Modifier,
    showErrorOnFirst: Boolean = false,
    imeAction: ImeAction = ImeAction.Done
) {
    OutlinedTextField(
        value = state.text,
        onValueChange = {
            state.text = it
            if (showErrorOnFirst) state.enableShowErrors()
        },
        modifier = modifier
            .fillMaxWidth()
            .sizeIn(maxWidth = 300.dp)
            .onFocusChanged { focusState ->
                state.onFocusChange(focusState.isFocused)
                if (!focusState.isFocused) {
                    state.enableShowErrors()
                }
            },
        maxLines = 1,
        label = { Text(text = label) },
        isError = state.isError(),
        supportingText = {
            state.getError()?.let { error -> TextFieldError(textError = error) }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Text
        )
    )
}

@Composable
fun TextFieldError(textError: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = textError,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.error
        )
    }
}