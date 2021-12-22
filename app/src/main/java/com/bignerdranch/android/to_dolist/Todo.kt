package com.bignerdranch.android.to_dolist

import java.util.*

// Our todo class
data class Todo(val id: UUID = UUID.randomUUID(), val title: String = "", var date: Date = Date(), var todoCheckBox : Boolean = false)