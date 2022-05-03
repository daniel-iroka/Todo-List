package com.bignerdranch.android.to_dolist.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import com.bignerdranch.android.to_dolist.Todo

// This will be our data access objects file where we will be updating, deleting and adding Todos to our database

@Dao
interface TodoDao {


    @Insert
    fun addTodo(todo: Todo)   // This is the function that will add todos to our database

    @Update
    fun updateTodo(todo: Todo)   // and this will update todos to our database
}