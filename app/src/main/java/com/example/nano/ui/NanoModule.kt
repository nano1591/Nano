package com.example.nano.ui

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.nano.data.dao.Api
import com.example.nano.data.dao.DB
import com.example.nano.data.dao.SP
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = NanoApplication.DATASTORE_NAME)

@Module
@InstallIn(SingletonComponent::class)
object NanoModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Api {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val httpClient = OkHttpClient
            .Builder()
            .addInterceptor(logger)
            .build()
        return Retrofit
            .Builder()
            .baseUrl(NanoApplication.HTTP_BASE_URL_DEBUG)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(Api::class.java)
    }

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): DB {
        return Room
            .databaseBuilder(context, DB::class.java, NanoApplication.DATABASE_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): SP {
        return SP(context.dataStore)
    }
}