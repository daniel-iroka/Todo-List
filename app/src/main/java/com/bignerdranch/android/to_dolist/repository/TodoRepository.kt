package com.bignerdranch.android.to_dolist.repository

import com.bignerdranch.android.to_dolist.data.TodoDao
import com.bignerdranch.android.to_dolist.model.Todo
import kotlinx.coroutines.flow.Flow

/**
 * This repository class abstracts access to multiple data sources(dao etc.) It hides the implementation behind data and provides access to the needed Information.
 */

class TodoRepository(private val todoDao : TodoDao) {

    suspend fun addTodo(todo : Todo) {
        todoDao.addTodo(todo)
    }

    suspend fun delSelectedTasks(idList: Long) {
        todoDao.deleteSelectedTasks(idList)
    }

    suspend fun delAllTasks() {
        todoDao.deleteAllTasks()
    }

    fun searchDatabase(queryText : String) : Flow<List<Todo>> {
        return todoDao.searchDatabase(queryText)
    }

    fun readAllDataByDateCreated() : Flow<List<Todo>> {
        return todoDao.readAllDataByDateCreated()
    }

    fun readAllDataByName() : Flow<List<Todo>> {
        return todoDao.readAllDataByName()
    }
}