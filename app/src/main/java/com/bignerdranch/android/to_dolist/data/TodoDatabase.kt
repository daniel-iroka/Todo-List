package com.bignerdranch.android.to_dolist.database

import androidx.room.Database
import com.bignerdranch.android.to_dolist.Todo

// This file will be our database

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase {

}