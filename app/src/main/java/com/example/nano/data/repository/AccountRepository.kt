package com.example.nano.data.repository

import com.example.nano.data.NanoResult
import com.example.nano.data.dao.AccountHttpDao
import com.example.nano.data.dao.AccountLocalDao
import com.example.nano.data.dao.DataStoreDao
import com.example.nano.data.exec
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map

class AccountRepository(
    private val accountHttpDao: AccountHttpDao,
    private val accountLocalDao: AccountLocalDao,
    private val dataStoreDao: DataStoreDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val isLogin = dataStoreDao.token.map { it.isNotEmpty() }
    val token = dataStoreDao.token
    suspend fun register(email: String, name: String, pwd: String, avatar: String) =
        exec { accountHttpDao.register(email, name, pwd, avatar) }.apply {
            if (this is NanoResult.Success) {
                dataStoreDao.setToken(data)
            }
        }

    suspend fun logout() =
        exec { accountHttpDao.logout() }.apply {
            if (this is NanoResult.Success) {
                dataStoreDao.setToken("")
            }
        }
}