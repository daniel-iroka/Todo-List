package com.bignerdranch.android.to_dolist.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bignerdranch.android.to_dolist.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Our AndroidViewModel. This AndroidViewModel holds reference to our Application context. **/

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    private val readAllData : LiveData<List<Todo>>
    private val repository : TodoRepository

    init {
        // having access to our TodoDao from our database
        val userDao = TodoDatabase.getDatabase(application).todoDao()
        repository = TodoRepository(userDao)
        readAllData = repository.readAllData
    }

    // This function using coroutines objects will run in a background thread
    fun addTodo(todo : Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(todo)
        }
    }

}