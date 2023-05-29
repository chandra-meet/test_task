package com.example.to_dolist.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.to_dolist.data.model.ToDoModel
import kotlinx.coroutines.flow.Flow


@Dao
interface ToDoDao {


    @Upsert
    suspend fun upsertToDo(toDoModel: ToDoModel)

    @Delete
    suspend fun deleteToDo(toDoModel: ToDoModel)

    @Query("SELECT * FROM tblToDo ORDER BY date ASC")
    fun getToDoListOrderByDate(): Flow<List<ToDoModel>>

    @Query("SELECT * FROM tblToDo ORDER BY title ASC")
    fun getToDoListOrderByTitle(): Flow<List<ToDoModel>>

    @Query("SELECT * FROM tblToDo ORDER BY status ASC")
    fun getToDoListOrderByStatus(): Flow<List<ToDoModel>>


}