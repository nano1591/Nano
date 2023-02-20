package com.example.nano.ui

import android.app.Application
import androidx.room.Room
import com.example.nano.data.NanoDatabase
import com.example.nano.data.repository.EmailRepository
import com.google.gson.Gson

class NanoApplication : Application() {
    companion object {
        lateinit var db: NanoDatabase
        lateinit var gson: Gson
        lateinit var emailRepository: EmailRepository
    }

    override fun onCreate() {
        super.onCreate()
        db = Room
            .databaseBuilder(this, NanoDatabase::class.java, "nano.db")
            .build()
        gson = Gson()
        emailRepository = EmailRepository(db)
    }
}