package com.bignerdranch.android.to_dolist.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.to_dolist.model.Todo

/** This will be our DAO file where we will be update, delete and add Todos to our database so it contains the methods used for accessing the database **/

@Dao
interface TodoDao {

    // onConflict will ignore any known conflicts, in this case will remove duplicate "Todos" with the same name
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTodo(todo: Todo)

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun readAllData() : List<LiveData<Todo>>

}