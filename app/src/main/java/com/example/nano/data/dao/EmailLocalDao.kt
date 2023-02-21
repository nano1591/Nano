package com.example.nano.data.dao

import androidx.room.*
import com.example.nano.data.model.Email
import kotlinx.coroutines.flow.Flow

@Dao
interface EmailLocalDao {
    @Query("select * from email order by updateAt, createdAt desc")
    fun getAll(): Flow<List<Email>>

    @Query("select * from email where eid = :eid")
    fun getOne(eid: Long): Flow<Email?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg email: Email)

    @Delete
    fun delete(email: Email)
}