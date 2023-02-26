package com.example.nano.ui

import android.app.Application
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.HiltAndroidApp

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
    }
}