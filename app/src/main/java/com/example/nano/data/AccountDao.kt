package com.example.nano.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Query("select * from account")
    fun getAll(): Flow<List<Account>>

    @Query("select * from account where uid in (:uid)")
    fun getAcct(vararg uid: Long): Flow<Account>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg account: Account)
}