package com.bignerdranch.android.to_dolist

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

// Our Model class. This class will represent our database table

// TODO - TOMORROW, TRY AND FIX THE GRADLE DEPENDENCY ERRORS FOR THIS PROJECT
// TODO - FIX HERE IS TO UPDATE THE ANDROID STUDIO VERSION I HOPE

@Entity
data class Todo(@PrimaryKey val id: UUID = UUID.randomUUID(), var title: String = "", var date: Date = Date(), var time: Date = Date(),
                var todoCheckBox : Boolean = false)