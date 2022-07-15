package com.bignerdranch.android.to_dolist.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

/** Our Model class. This class will represent our database table **/

@Parcelize
@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey (autoGenerate = true) // here "Room" will autoGenerate the id for us instead of assigning a randomUUID value
    val id : Int = 0,
    val title : String = "",
    var date : Date = Date(),
    var time : Date = Date(),
    var completed : Boolean = false,
    val important : Boolean = false,
    val created : Long = System.currentTimeMillis()
) : Parcelable
