package com.example.nano.ui.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue

open class TextFieldState(
    private val defaultText: String = "",
    private val validators: List<(String) -> String?> = emptyList(),
) {
    var text: String by mutableStateOf(defaultText)
    var isFocusedDirty: Boolean by mutableStateOf(false)
    var isFocused: Boolean by mutableStateOf(false)
    private var displayErrors: Boolean by mutableStateOf(false)

    open val isValid: Boolean
        get() = validator(text)

    fun onFocusChange(focused: Boolean) {
        isFocused = focused
        if (focused) isFocusedDirty = true
    }

    fun showErrors() = !isValid && displayErrors

    open fun getError(): String? {
        return if (showErrors()) {
            errMsg
        } else {
            null
        }
    }

    private fun validator(): String? = {
        validators.find { validator ->
            validator(text).isNotEmpty()
        }?.let {

        }
    }
}