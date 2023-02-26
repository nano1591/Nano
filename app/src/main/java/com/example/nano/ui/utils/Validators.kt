package com.example.nano.ui.utils

object Validators {
    private fun match(value: String, regex: Regex, errorText: String): String? =
        if (regex.matches(value)) errorText else null

    private fun notMatch(value: String, regex: Regex): String = regex.replace(value, "")

    fun required(value: String): String? =
        match(value, Regex("^.$"), "必选项")

    fun word(value: String): String? =
        match(
            value = value,
            regex = Regex("^\\w+$"),
            errorText = "非法字符:" + notMatch(value, Regex("^\\w+$"))
        )
}