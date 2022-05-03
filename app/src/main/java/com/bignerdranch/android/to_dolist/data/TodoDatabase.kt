package com.bignerdranch.android.to_dolist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.to_dolist.model.Todo

/** This file will be our Database **/

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    // will return our Dao as well as its objects
    abstract fun todoDao() : TodoDao

    companion object {
        val INSTANCE : TodoDatabase? = null

    }
}