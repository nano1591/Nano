package com.example.nano.ui.utils

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun String.ShowToast() {
    Toast.makeText(LocalContext.current, this, Toast.LENGTH_SHORT).show()
}

suspend fun <T> MutableStateFlow<T>.emitData(reduce: T.() -> T) {
    emit(value.reduce())
}