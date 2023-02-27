package com.example.nano.ui.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class TextFieldState(
    private val defaultText: String = "",
    private val validators: List<(String) -> String?> = emptyList(),
) {
    var text: String by mutableStateOf(defaultText)
    var isFocusedOnce: Boolean by mutableStateOf(false)
    var isFocus: Boolean by mutableStateOf(false)
    private var displayErrors: Boolean by mutableStateOf(false)

    open val isValid: Boolean
        get() = validator()?.isEmpty() ?: true

    fun onFocusChange(focused: Boolean) {
        isFocus = focused
        if (focused) isFocusedOnce = true
    }

    fun enableShowErrors() {
        if (isFocusedOnce) {
            displayErrors = true
        }
    }

    fun isError() = !isValid && displayErrors

    open fun getError(): String? {
        return if (isError()) {
            validator()
        } else {
            null
        }
    }

    private fun validator() =
        validators.find { validator ->
            validator(text)?.isNotEmpty() ?: false
        }?.let {
            it(text)
        }
}