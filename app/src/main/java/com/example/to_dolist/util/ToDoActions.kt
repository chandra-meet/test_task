package com.example.to_dolist.util

import com.example.to_dolist.data.model.ToDoModel
import java.util.Date

sealed interface ToDoActions {
    object AddToDo : ToDoActions

    data class SetTitle(val title: String) : ToDoActions
    data class SetId(val id: Int) : ToDoActions
    data class SetDate(val date: Date) : ToDoActions
    data class SetStatus(val status: String) : ToDoActions
    data class SortToDoList(val sortType: SortType) : ToDoActions
    data class DeleteToDo(val toDo: ToDoModel) : ToDoActions

}