package com.example.nano.ui.utils

class Validators {
    private val validators = listOf<(String) -> String?>().toMutableList()

    private fun matchOrError(match: (String) -> Boolean, errorOf: (String) -> String): Validators {
        validators.add {
            if (match(it)) null else errorOf(it)
        }
        return this
    }

    fun merge(): List<(String) -> String?> = validators.toList()

    fun required(errorText: String? = null) =
        matchOrError(
            match = { it.isNotEmpty() },
            errorOf = { errorText ?: "此项为必选项" }
        )

    fun word(errorText: String? = null) =
        matchOrError(
            match = { Regex("^\\w+$").matches(it) },
            errorOf = {
                errorText ?: ("非法字符:" + it.replace(Regex("\\w"), ""))
            }
        )

    fun minLength(min: Int, errorText: String? = null) =
        matchOrError(
            match = { it.length >= min },
            errorOf = { errorText ?: "最小长度：$min" }
        )

    fun maxLength(max: Int, errorText: String? = null) =
        matchOrError(
            match = { it.length < max },
            errorOf = { errorText ?: "最大长度：$max" }
        )

    fun equal(actual: String, errorText: String? = null) =
        matchOrError(
            match = { it == actual },
            errorOf = { errorText ?: "与期望值不符" }
        )

    fun equal(actual: TextFieldState, errorText: String? = null) =
        matchOrError(
            match = { it == actual.text },
            errorOf = { errorText ?: "与期望值不符" }
        )
}
