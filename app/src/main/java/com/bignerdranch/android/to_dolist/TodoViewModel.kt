package com.bignerdranch.android.to_dolist

import androidx.lifecycle.ViewModel

class TodoViewModel : ViewModel() {

    val todos = mutableListOf<Todo>()

    // Created a dummy list to test out our Lists
    init {
        for (i in 0 until 100) {
            val todo = Todo()
            todo.title = "Todo Test #$i"
        }
    }
}