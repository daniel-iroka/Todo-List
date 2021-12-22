package com.bignerdranch.android.to_dolist

import androidx.lifecycle.ViewModel

class TodoViewModel : ViewModel() {

    val todos = mutableListOf<Todo>()
}