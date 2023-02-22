package com.example.nano.ui

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.nano.data.NanoDatabase
import com.example.nano.data.dao.AccountHttpDao
import com.example.nano.data.dao.DataStoreDao
import com.example.nano.data.initRetrofit
import com.example.nano.data.repository.EmailRepository
import com.example.nano.data.repository.AccountRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit

const val DATASTORE_NAME = "nano-datastore"
const val DATABASE_NAME = "nano.db"

const val HTTP_BASE_URL = "http://domain:port/"
const val HTTP_BASE_URL_DEBUG = "http://10.0.2.2:10086/"

class NanoApplication : Application() {
    private val _dataStore: DataStore<Preferences> by preferencesDataStore(
        name = DATASTORE_NAME
    )

    override fun onCreate() {
        super.onCreate()
        db = Room
            .databaseBuilder(this, NanoDatabase::class.java, DATABASE_NAME)
            .build()
        dataStore = _dataStore
    }

    companion object {
        private lateinit var db: NanoDatabase
        private lateinit var dataStore: DataStore<Preferences>
        val moshi: Moshi by lazy {
            Moshi
                .Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
        }
        private val retrofit: Retrofit by lazy {
            initRetrofit(HTTP_BASE_URL_DEBUG)
        }
        private val dataStoreDao: DataStoreDao by lazy {
            DataStoreDao(dataStore)
        }
        val emailRepository: EmailRepository by lazy {
            EmailRepository(db.emailLocalDao())
        }
        val accountRepository: AccountRepository by lazy {
            AccountRepository(
                accountHttpDao = retrofit.create(AccountHttpDao::class.java),
                accountLocalDao = db.accountLocalDao(),
                dataStoreDao = dataStoreDao
            )
        }
    }
}