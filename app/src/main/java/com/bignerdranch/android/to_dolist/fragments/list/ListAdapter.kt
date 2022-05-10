package com.bignerdranch.android.to_dolist.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bignerdranch.android.to_dolist.databinding.CustomRowBinding
import com.bignerdranch.android.to_dolist.fragments.add.SIMPLE_DATE_FORMAT
import com.bignerdranch.android.to_dolist.fragments.add.SIMPLE_TIME_FORMAT
import com.bignerdranch.android.to_dolist.model.Todo
import java.text.SimpleDateFormat
import java.util.Locale

class ListAdapter(): Adapter<ListAdapter.TodoViewHolder>() {
    private var todoList = emptyList<Todo>()

    // TODO - WHEN I COME BACK, I WILL IMPLEMENT THIS COMPLETELY

    inner class TodoViewHolder(val binding : CustomRowBinding) : RecyclerView.ViewHolder(binding.root) { }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        // this can be done in an inline variable and I may experiment on it later.
        val binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return TodoViewHolder(binding)

    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todoList[position]
        val dateLocales = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
        val timeLocales = SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.getDefault())
        holder.apply {
            binding.tvTaskTitle.text = todo.title
            binding.tvTaskDate.text = dateLocales.format(todo.date)
            binding.tvTaskTime.text = timeLocales.format(todo.time)
            // TODO - WORK ON THE CHECKBOX BY EITHER FOLLOWING PHILLIP'S EXAMPLE OR LOOKING FOR A BETTER ON
            binding.cbTask
        }
    }

    // as usual will return the size of the List
    override fun getItemCount() = todoList.size
}