package com.example.nano.data.dao

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class SP(
    private val dataStore: DataStore<Preferences>
) {
    val token = SpColumn(stringPreferencesKey("login_token"))

    val userAccount = SpColumn(stringPreferencesKey("user_account"))

    val userPassword = SpColumn(stringPreferencesKey("user_password"))

    inner class SpColumn<T>(private val KEY: Preferences.Key<T>) {
        fun get() = dataStore.data.map { it[KEY] }
        suspend fun set(value: T) = dataStore.edit { it[KEY] = value }
    }

    // TODO list数据的保存
}

