package com.bignerdranch.android.to_dolist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/** Our Model class. This class will represent our database table **/

// TODO - WHEN I COME BACK, I WILL LOOK A LITTLE ON KOTLIN "Coroutines" AND ITS FUNCTIONS ALSO IN RELATION WITH THE VIDEO PROJECT I AM FOLLOWING.

@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey (autoGenerate = true) // here "Room" will autoGenerate the id for us instead of assigning a randomUUID value
    val id : Int,
    var title : String = "",
    var date : Date = Date(),
    var time : Date = Date(),
    var todoCheckBox : Boolean = false
)