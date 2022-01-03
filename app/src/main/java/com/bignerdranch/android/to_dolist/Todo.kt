package com.bignerdranch.android.to_dolist

import java.util.*

// Our todo class
data class Todo(val id: UUID = UUID.randomUUID(), var title: String = "", var date: Date = Date(), var time: Date = Date(), var todoCheckBox : Boolean = false)