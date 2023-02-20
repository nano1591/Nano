package com.example.nano.data.repository

import com.example.nano.data.Email
import com.example.nano.data.EmailWrapper
import com.example.nano.data.NanoDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class EmailRepository(
    private val db: NanoDatabase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun allEmailFromDB() = withContext(ioDispatcher) {
        db.emailDao().getAll()
            .map { emails ->
                EmailWrapper(emails, true)
            }
    }

    suspend fun add(email: Email) = withContext(ioDispatcher) {
        db.emailDao().insert(email)
    }

    suspend fun delete(email: Email) = withContext(ioDispatcher) {
        db.emailDao().delete(email)
    }
}