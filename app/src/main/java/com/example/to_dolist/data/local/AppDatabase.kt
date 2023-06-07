package com.example.to_dolist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.to_dolist.data.model.ToDoModel
import com.example.to_dolist.util.DateConverterType

@Database(
    version = 3, entities = [ToDoModel::class], exportSchema = false
)
@TypeConverters(DateConverterType::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val toDoDao: ToDoDao

}