package com.bignerdranch.android.to_dolist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/** Our Model class. This class will represent our database table **/


@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey (autoGenerate = true) // here "Room" will autoGenerate the id for us instead of assigning a randomUUID value
    val id : Int = 0,
    var title : String = "",
    var date : Date = Date(),
    var time : Date = Date(),
    var todoCheckBox : Boolean = false
)