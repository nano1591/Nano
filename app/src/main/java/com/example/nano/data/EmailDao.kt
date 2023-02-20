package com.example.nano.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmailDao {
    @Query("select * from email order by viewAt, createdAt desc")
    fun getAll(): Flow<List<Email>>

    @Query("select * from email where eid = :eid")
    fun getOne(eid: Long): Flow<Email?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg email: Email)

    @Delete
    fun delete(email: Email)
}