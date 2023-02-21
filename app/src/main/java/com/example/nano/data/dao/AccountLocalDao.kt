package com.example.nano.data.dao

import androidx.room.*
import com.example.nano.data.model.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountLocalDao {
    @Query("select * from account")
    fun getAll(): Flow<List<Account>>

    @Query("select * from account where uid in (:uid)")
    fun getAcct(vararg uid: Long): Flow<Account>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg account: Account)
}