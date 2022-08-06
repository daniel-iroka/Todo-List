package com.bignerdranch.android.to_dolist.data

import androidx.room.*
import com.bignerdranch.android.to_dolist.model.Todo
import kotlinx.coroutines.flow.Flow

/**
 *  This will be our DAO file where we will be update, delete and add Todos to our database so it contains the methods used for accessing the database
 */

@Dao
interface TodoDao {

    // function to hold all out queries and will be executed based on our sortOrder
    fun getAllTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Todo>> =
        when(sortOrder) {
            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query, hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted)
        }

    @Query("SELECT * FROM todo_table WHERE (completed != :hideCompleted OR completed = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY important DESC, title COLLATE NOCASE")
    fun getTasksSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Todo>>

    @Query("SELECT * FROM todo_table WHERE (completed != :hideCompleted OR completed = 0) AND title LIKE '%' || :searchQuery || '%' ORDER BY important DESC, created")
    fun getTasksSortedByDateCreated(searchQuery: String, hideCompleted: Boolean): Flow<List<Todo>>

    // onConflict will ignore any known conflicts, in this case will remove duplicate "Todos" with the same name
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTodo(todo: Todo)

    @Query("DELETE FROM todo_table WHERE id IN (:idList)")
    suspend fun deleteCompletedTasks(idList : Long)

    @Delete
    suspend fun deleteTask(todo : Todo)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTasks()

    @Update
    suspend fun updateTask(todo : Todo)
}
