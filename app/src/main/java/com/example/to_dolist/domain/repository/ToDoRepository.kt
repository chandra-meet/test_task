package com.example.to_dolist.domain.repository

import com.example.to_dolist.data.model.ToDoModel
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {

    suspend fun upsertToDo(toDoModel: ToDoModel)

    suspend fun deleteToDo(toDoModel: ToDoModel)

    fun getToDoListOrderByDate(): Flow<List<ToDoModel>>

    fun getToDoListOrderByTitle(): Flow<List<ToDoModel>>

    fun getToDoListOrderByStatus(): Flow<List<ToDoModel>>

}