package com.example.to_dolist.domain.state

import com.example.to_dolist.data.model.ToDoModel
import com.example.to_dolist.util.SortType
import java.util.*

data class ToDoModelState(
    val todoList: List<ToDoModel> = emptyList(),
    var id: Int = 0,
    var title: String = "",
    var date: String = "",
    var status: String = "",
    val isAddingToDo: Boolean = false,
    val sortType: SortType = SortType.DATE,
)
