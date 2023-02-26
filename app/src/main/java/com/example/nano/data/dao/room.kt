package com.example.nano.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.nano.data.Account
import com.example.nano.data.Email
import com.example.nano.data.EmailAttachment
import com.example.nano.ui.utils.MoshiKit
import java.util.*

@Database(entities = [Account::class, Email::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class DB : RoomDatabase() {
    abstract fun acct(): AccountDao

    abstract fun email(): EmailDao
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun stringToEmailAttachment(json: String): List<EmailAttachment> {
        return MoshiKit.fromJsonToList(json)!!
    }

    @TypeConverter
    fun attachmentToString(attachment: List<EmailAttachment>): String {
        return MoshiKit.toJson(attachment)
    }

    @TypeConverter
    fun longToString(long: List<Int>): String {
        return MoshiKit.toJson(long)
    }

    @TypeConverter
    fun stringToLongList(json: String): List<Int> {
        return MoshiKit.fromJsonToList(json)!!
    }
}

