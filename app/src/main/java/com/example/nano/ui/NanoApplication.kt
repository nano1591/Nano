package com.example.nano.ui

import android.app.Application
import androidx.compose.ui.graphics.vector.ImageVector
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.MutableStateFlow

@HiltAndroidApp
class NanoApplication : Application() {
    companion object {
        const val DATASTORE_NAME = "nano-sp"
        const val DATABASE_NAME = "nano.db"
        const val HTTP_BASE_URL = "http://domain:port/"
        const val HTTP_BASE_URL_DEBUG = "http://10.0.2.2:10086/"

        val moshi: Moshi = Moshi
            .Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val appUiState = MutableStateFlow(AppUiState())
        // TODO api调用过程中的错误处理，ApiAction
    }
}

data class AppUiState(
    val isLoading: Boolean = false,
    val dialog: List<NanoDialog?> = emptyList()
)

data class NanoDialog(
    val onDismissRequest: () -> Unit,
    val confirmButton: Btn,
    val dismissButton: Btn? = null,
    val icon: ImageVector? = null,
    val title: String? = null,
    val text: String? = null,
)

data class Btn(
    val icon: ImageVector? = null,
    val text: String? = null,
    val onClick: () -> Unit
)