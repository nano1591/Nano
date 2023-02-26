package com.example.nano.data.dao

import androidx.room.*
import com.example.nano.data.Account
import com.example.nano.data.Email
import kotlinx.coroutines.flow.Flow

@Dao
interface EmailDao {
    @Query("select * from email order by updateAt, createdAt desc")
    fun getAll(): Flow<List<Email>>

    @Query("select * from email where eid = :eid")
    fun getOne(eid: Long): Flow<Email?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg email: Email)

    @Delete
    fun delete(email: Email)
}

@Dao
interface AccountDao {
    @Query("select * from account")
    fun getAll(): Flow<List<Account>>

    @Query("select * from account where uid in (:uid)")
    fun getAcct(vararg uid: Long): Flow<Account>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg account: Account)
}