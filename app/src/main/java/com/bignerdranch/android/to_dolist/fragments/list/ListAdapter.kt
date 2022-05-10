package com.bignerdranch.android.to_dolist.fragments.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bignerdranch.android.to_dolist.model.Todo

class ListAdapter(): Adapter<ListAdapter.TodoViewHolder>() {
    private var todoList = emptyList<Todo>()

    // TODO - WHEN I COME BACK, I WILL IMPLEMENT THIS COMPLETELY

    inner class TodoViewHolder(item : View) : RecyclerView.ViewHolder(item) { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    // as usual will return the size of the List
    override fun getItemCount() = todoList.size
}