package com.bignerdranch.android.to_dolist.data

import android.content.Context
import androidx.room.*
import com.bignerdranch.android.to_dolist.model.Todo

/** This file will be our Database **/

@Database(entities = [Todo::class], version = 1, exportSchema = false)
@TypeConverters(TodoTypeConverters::class)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao() : TodoDao

    companion object {
        @Volatile
        var INSTANCE : TodoDatabase? = null


        /** This whole block basically checks if an instance of this database exists and returns that same instance but if not, creates a new one.  **/

        fun getDatabase(context : Context) : TodoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            // synchronized method here provides a "lock" within its block to protect whatever is in it from being executed by other threads
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "table_todo"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}