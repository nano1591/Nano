package com.example.nano.data.dao

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class DataStoreDao(
    private val dataStore: DataStore<Preferences>
) {
    val token = dataStore.data.map { it[KEY_TOKEN] ?: "" }
    suspend fun setToken(token: String) = dataStore.edit { it[KEY_TOKEN] = token }

    val userAccount = dataStore.data.map { it[KEY_USER_ACCOUNT] ?: "" }
    suspend fun setUserAccount(account: String) = dataStore.edit { it[KEY_USER_ACCOUNT] = account }

    val userPassword = dataStore.data.map { it[KEY_USER_PASSWORD] ?: "" }
    suspend fun setUserPassword(pwd: String) = dataStore.edit { it[KEY_USER_PASSWORD] = pwd }

    companion object {
        val KEY_TOKEN = stringPreferencesKey("login_token")
        val KEY_USER_ACCOUNT = stringPreferencesKey("user_account")
        val KEY_USER_PASSWORD = stringPreferencesKey("user_password")
    }
}