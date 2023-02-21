package com.example.nano.data

sealed class NanoResult<out R> {
    data class Success<out T>(val data: T) : NanoResult<T>()
    data class Error(val exception: Exception) : NanoResult<Nothing>()
}

fun <T> NanoResult<T>.successOr(fallback: T): T {
    return (this as? NanoResult.Success<T>)?.data ?: fallback
}

data class HttpResult<T>(val code: String, val msg: String, val data: T)