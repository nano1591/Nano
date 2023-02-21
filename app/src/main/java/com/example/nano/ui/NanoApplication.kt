package com.example.nano.ui

import android.app.Application
import androidx.room.Room
import com.example.nano.data.NanoDatabase
import com.example.nano.data.dao.AccountHttpDao
import com.example.nano.data.initRetrofit
import com.example.nano.data.repository.EmailRepository
import com.example.nano.data.repository.AccountRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit

const val HTTP_BASE_URL = "http://#####"
const val HTTP_BASE_URL_DEBUG = "http://10.0.0.2:8888"

class NanoApplication : Application() {
    companion object {
        lateinit var db: NanoDatabase
        lateinit var moshi: Moshi
        lateinit var retrofit: Retrofit
        lateinit var emailRepository: EmailRepository
        lateinit var accountRepository: AccountRepository
    }

    override fun onCreate() {
        super.onCreate()
        db = Room
            .databaseBuilder(this, NanoDatabase::class.java, "nano.db")
            .build()
        moshi = Moshi
            .Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        retrofit = initRetrofit(HTTP_BASE_URL_DEBUG)
        emailRepository = EmailRepository(db.emailLocalDao())
        accountRepository = AccountRepository(
            accountHttpDao = retrofit.create(AccountHttpDao::class.java),
            accountLocalDao = db.accountLocalDao()
        )
    }
}