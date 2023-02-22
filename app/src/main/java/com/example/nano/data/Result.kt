package com.example.nano.data

import com.squareup.moshi.JsonClass
import java.net.ConnectException

sealed class NanoResult<out R> {
    // code为0 请求成功
    data class Success<out T>(val data: T) : NanoResult<T>()

    // code不为0 业务error
    data class ApiError(val code: Int, val msg: String) : NanoResult<Nothing>()

    // 网络问题
    object ConnectException : NanoResult<Nothing>()

    // io
    data class IOError(val error: Exception) : NanoResult<Nothing>()
}

fun <T> NanoResult<T>.successOr(fallback: T): T {
    return (this as? NanoResult.Success<T>)?.data ?: fallback
}

@JsonClass(generateAdapter = true)
data class HttpResult<T>(val code: Int, val msg: String, val data: T)

suspend fun <T> exec(block: suspend () -> HttpResult<T>): NanoResult<T> {
    return try {
        val result = block.invoke()
        if (result.code == 0) {
            NanoResult.Success(result.data)
        } else {
            NanoResult.ApiError(result.code, result.msg)
        }
    } catch (e: ConnectException) {
        e.printStackTrace()
        NanoResult.ConnectException
    } catch (e: Exception) {
        e.printStackTrace()
        NanoResult.IOError(e)
    }
}