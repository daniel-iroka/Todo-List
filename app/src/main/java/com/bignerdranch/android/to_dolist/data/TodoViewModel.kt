package com.bignerdranch.android.to_dolist.data

import android.app.Application
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.*
import com.bignerdranch.android.to_dolist.model.Todo
import com.bignerdranch.android.to_dolist.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/** Our AndroidViewModel. This AndroidViewModel holds reference to our Application context. **/

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    /**
     *  NOTE! : "Context" are needed to instantiate a database that is why we are using an AndroidViewModel in this case because it holds reference to an
     *  Application context. And if I remember correctly, it will start as the "Application" starts.
     **/

    val readAllData : LiveData<List<Todo>>
    private val repository : TodoRepository

    val sortOrder = MutableStateFlow(SortOrder.BY_DATE) // we're adding BY_DATE here because we want the tasks to be sorted by date by default
    val hideCompleted = MutableStateFlow(false)

    init {
        // having access to our TodoDao from our database
        val userDao = TodoDatabase.getDatabase(application).todoDao()
        repository = TodoRepository(userDao)
        readAllData = repository.readAllData
    }

    // All functions using coroutines objects indicates that whatever is in it should run in a background thread
    fun addTodo(todo : Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(todo)
        }
    }

    fun deleteSelectedTasks(idList: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delSelectedTasks(idList)
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delAllTasks()
        }
    }

    fun searchDatabase(queryText : String) : LiveData<List<Todo>> {
        return repository.searchDatabase(queryText).asLiveData()
    }

    fun readAllDataByDateCreated() : LiveData<List<Todo>> {
        return repository.readAllDataByDateCreated().asLiveData()
    }

    fun readAllDataByName() : LiveData<List<Todo>> {
        return repository.readAllDataByName().asLiveData()
    }
}

enum class SortOrder { BY_DATE, BY_NAME }















