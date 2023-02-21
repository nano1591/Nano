package com.example.nano.data.repository

import android.util.Log
import com.example.nano.data.dao.AccountLocalDao
import com.example.nano.data.dao.AccountHttpDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountRepository(
    private val accountHttpDao: AccountHttpDao,
    private val accountLocalDao: AccountLocalDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun register(email: String, name: String, pwd: String, avatar: String) =
        withContext(ioDispatcher) {
            Log.d("register", accountHttpDao.register(email, name, pwd, avatar))
            //        when (val result = accountHttpDao.register(email, name, pwd, avatar)) {
//            is NanoResult.Success -> accountLocalDao.insert(result.data)
//            is NanoResult.Error -> Log.e("register", result.exception.toString())
//        }
        }
}