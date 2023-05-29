package com.example.to_dolist.util

import androidx.room.TypeConverter
import java.util.*


object DateConverterType {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}