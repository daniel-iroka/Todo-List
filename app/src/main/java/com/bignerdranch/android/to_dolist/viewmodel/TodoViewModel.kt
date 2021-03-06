package com.bignerdranch.android.to_dolist.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.bignerdranch.android.to_dolist.data.TodoDatabase
import com.bignerdranch.android.to_dolist.model.Todo
import com.bignerdranch.android.to_dolist.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/** Our AndroidViewModel. This AndroidViewModel holds reference to our Application context. **/

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    /**
     *  NOTE! : "Context" are needed to instantiate a database that is why we are using an AndroidViewModel in this case because it holds reference to an
     *  Application context. And if I remember correctly, it will start as the "Application" starts.
     **/

    private val repository : TodoRepository
    private val todoDao = TodoDatabase.getDatabase(application).todoDao()

    init {
        // having access to our TodoDao from our database
        val userDao = TodoDatabase.getDatabase(application).todoDao()
        repository = TodoRepository(userDao)
    }


    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_DATE)   // adding BY_DATE to make the lists sorted by date as default
    val hideCompleted = MutableStateFlow(false)


    /**
     *  The combine function here is a an object in the flow library that is used too combine the most recent values of a flow, so if one value changes it will
     *  automatically return the latest values of the other flows. This is done so that the three flows will work in harmony.
     */
    private val tasksFlow = combine(
        searchQuery,
        sortOrder,
        hideCompleted
    ) { query, sortOrder, hideCompleted -> // LAMBDA
        Triple(query, sortOrder, hideCompleted)
        // flatMapLatest gets triggered when any of this flows changes and then passes it to the query to be executed.
    }.flatMapLatest { (query, sortOrder, hideCompleted) ->
        repository.getAllTasks(query, sortOrder, hideCompleted)
    }

    val tasks = tasksFlow.asLiveData()


    // All functions using coroutines objects indicates that whatever is in it should run in a background thread
    fun addTodo(todo : Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(todo)
        }
    }

    fun deleteCompletedTasks(idList: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delCompletedTasks(idList)
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delAllTasks()
        }
    }

    fun deleteTask(todo : Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delTask(todo)
        }
    }

    fun updateTask(todo : Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTask(todo)
        }
    }

    fun onTaskCheckedChanged(todo : Todo, isChecked : Boolean) {
        viewModelScope.launch {
            repository.updateTask(todo.copy(completed = isChecked))
        }
    }
}

enum class SortOrder { BY_DATE, BY_NAME }















