package com.bignerdranch.android.to_dolist.data

import androidx.lifecycle.LiveData
import com.bignerdranch.android.to_dolist.model.Todo

/**
 * This repository class abstracts access to multiple data sources(dao etc.) It hides the implementation behind data and provides access to the needed Information.
 */

/**class TodoRepository(private val todoDao : TodoDao) {

    val readAllData : LiveData<List<Todo>> = todoDao.readAllData()

    suspend fun addTodo(todo : Todo) {
        todoDao.addTodo(todo)
    }
}**/