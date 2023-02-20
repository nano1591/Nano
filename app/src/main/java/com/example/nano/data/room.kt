package com.example.nano.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.nano.ui.NanoApplication
import com.google.gson.reflect.TypeToken
import java.util.*

@Database(entities = [Account::class, Email::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NanoDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao

    abstract fun emailDao(): EmailDao
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
    fun stringToEmailAttachment(attachment: String): List<EmailAttachment> {
        return NanoApplication.gson.fromJson(
            attachment,
            object : TypeToken<List<EmailAttachment>>() {}.type
        )
    }

    @TypeConverter
    fun attachmentToString(attachment: List<EmailAttachment>): String {
        return NanoApplication.gson.toJson(attachment)
    }

    @TypeConverter
    fun longToString(long: List<Long>): String = NanoApplication.gson.toJson(long)

    @TypeConverter
    fun stringToLongList(string: String): List<Long> {
        return NanoApplication.gson.fromJson(string, object : TypeToken<List<Long>>() {}.type)
    }
}

