package com.example.to_dolist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_dolist.data.model.ToDoModel
import com.example.to_dolist.domain.repository.ToDoRepository
import com.example.to_dolist.domain.state.ToDoModelState
import com.example.to_dolist.util.SortType
import com.example.to_dolist.util.ToDoActions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class MainActivityViewModel @Inject constructor(private val toDoRepository: ToDoRepository) :
    ViewModel() {


    private val _sortType = MutableStateFlow(SortType.DATE)

    private val _todolist = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            SortType.DATE -> toDoRepository.getToDoListOrderByDate()
            SortType.STATUS -> toDoRepository.getToDoListOrderByStatus()
            SortType.TITLE -> toDoRepository.getToDoListOrderByTitle()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    private val _state = MutableStateFlow(ToDoModelState())
    var state = combine(_state, _sortType, _todolist) { state, sortType, todolist ->
        state.copy(
            todoList = todolist,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), ToDoModelState())


    fun onToDoAction(action: ToDoActions) {

        when (action) {

            is ToDoActions.DeleteToDo -> {
                viewModelScope.launch {
                    toDoRepository.deleteToDo(action.toDo)
                }
            }

            is ToDoActions.SetDate -> {
                _state.update {
                    it.copy(
                        date = action.date
                    )
                }
            }
            is ToDoActions.SetId -> {
                _state.update {
                    it.copy(
                        id = action.id
                    )
                }
            }

            is ToDoActions.SetTitle -> {
                _state.update {
                    it.copy(title = action.title)
                }
            }

            is ToDoActions.SetStatus -> {
                _state.update {
                    it.copy(status = action.status)
                }
            }

            is ToDoActions.SortToDoList -> {
                _sortType.value = action.sortType
            }

            ToDoActions.AddToDo -> {

                val id = _state.value.id
                val title = _state.value.title
                val date = _state.value.date
                val status = _state.value.status

                if (title.isBlank()) {
                    return
                }

                val todo =
                    if (id > 0) {
                        ToDoModel(title = title, date = date, status = status, id = id)
                    } else {
                        ToDoModel(title = title, date = date, status = status)
                    }

                viewModelScope.launch {
                    toDoRepository.upsertToDo(todo)
                }

                _state.update {
                    it.copy(
                        id = 0,
                        isAddingToDo = false,
                        title = "",
                        date = "",
                        status = ""
                    )
                }

            }

            /*   ToDoActions.ShowDialog -> {
                   _state.update {
                       it.copy(
                           isAddingToDo = true
                       )
                   }
               }

               ToDoActions.HideDialog -> {
                   _state.update {
                       it.copy(
                           isAddingToDo = false
                       )
                   }
               }*/


        }

    }


}