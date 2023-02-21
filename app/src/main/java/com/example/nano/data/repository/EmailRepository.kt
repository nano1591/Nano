package com.example.nano.data.repository

import com.example.nano.data.dao.EmailLocalDao
import com.example.nano.data.model.Email
import com.example.nano.data.model.EmailWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class EmailRepository(
    private val emailLocalDao: EmailLocalDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun allEmailFromDB() = withContext(ioDispatcher) {
        emailLocalDao.getAll()
            .map { emails ->
                EmailWrapper(emails, true)
            }
    }

    suspend fun add(email: Email) = withContext(ioDispatcher) {
        emailLocalDao.insert(email)
    }

    suspend fun delete(email: Email) = withContext(ioDispatcher) {
        emailLocalDao.delete(email)
    }
}