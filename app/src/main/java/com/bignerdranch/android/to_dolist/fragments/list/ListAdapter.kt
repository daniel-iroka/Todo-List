package com.bignerdranch.android.to_dolist.fragments.list

import android.annotation.SuppressLint
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bignerdranch.android.to_dolist.R
import com.bignerdranch.android.to_dolist.databinding.CustomRowBinding
import com.bignerdranch.android.to_dolist.fragments.add.SIMPLE_DATE_FORMAT
import com.bignerdranch.android.to_dolist.fragments.add.SIMPLE_TIME_FORMAT
import com.bignerdranch.android.to_dolist.model.Todo
import java.text.SimpleDateFormat
import java.util.Locale


class ListAdapter: Adapter<ListAdapter.TodoViewHolder>() {
    private var todoList = emptyList<Todo>()


    // will toggle strikeThrough on the Task title
    private fun toggleStrikeThrough(tvTaskTitle : TextView, cbTask : Boolean) {
        if (cbTask) {
            tvTaskTitle.paintFlags = tvTaskTitle.paintFlags  or STRIKE_THRU_TEXT_FLAG
        } else {
            tvTaskTitle.paintFlags = tvTaskTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    inner class TodoViewHolder(val binding : CustomRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        // this can be done in an inline variable and I may experiment on it later.
        val binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false
        )
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
            binding.cbTask.isChecked = todo.todoCheckBox

            toggleStrikeThrough(binding.tvTaskTitle , todo.todoCheckBox)
            binding.cbTask.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(binding.tvTaskTitle, isChecked)
                todo.todoCheckBox = !todo.todoCheckBox
            }
        }
    }

    // as usual will return the size of the List
    override fun getItemCount() = todoList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(todo : List<Todo>) {
        this.todoList = todo
        // todo - This is a suppressed lint warning. Later check it online and see if there is a way to improve it.
        notifyDataSetChanged()
    }
}