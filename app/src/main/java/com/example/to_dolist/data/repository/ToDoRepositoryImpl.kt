package com.example.to_dolist.data.repository

import com.example.to_dolist.data.local.ToDoDao
import com.example.to_dolist.data.model.ToDoModel
import com.example.to_dolist.domain.repository.ToDoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ToDoRepositoryImpl @Inject constructor(private val toDoDao: ToDoDao) : ToDoRepository {

    override suspend fun upsertToDo(toDoModel: ToDoModel) {
        toDoDao.upsertToDo(toDoModel)
    }

    override suspend fun deleteToDo(toDoModel: ToDoModel) {
        toDoDao.deleteToDo(toDoModel)
    }

    override fun getToDoListOrderByDate(): Flow<List<ToDoModel>> =
        toDoDao.getToDoListOrderByDate()


    override fun getToDoListOrderByTitle(): Flow<List<ToDoModel>> =
        toDoDao.getToDoListOrderByTitle()

    override fun getToDoListOrderByStatus(): Flow<List<ToDoModel>> =
        toDoDao.getToDoListOrderByStatus()
}