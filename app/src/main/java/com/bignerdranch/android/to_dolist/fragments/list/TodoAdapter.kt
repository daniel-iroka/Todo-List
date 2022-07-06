package com.bignerdranch.android.to_dolist.fragments.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.bignerdranch.android.to_dolist.R
import com.bignerdranch.android.to_dolist.databinding.CustomRowBinding
import com.bignerdranch.android.to_dolist.fragments.add.SIMPLE_DATE_FORMAT
import com.bignerdranch.android.to_dolist.fragments.add.SIMPLE_TIME_FORMAT
import com.bignerdranch.android.to_dolist.model.Todo
import java.text.SimpleDateFormat
import java.util.*


class TodoAdapter(private val _context : Context, private val listener : OnItemClickListener): ListAdapter<Todo, TodoAdapter.TodoViewHolder>(DiffCallBack) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        // this can be done in an inline variable and I may experiment on it later.
        val binding = CustomRowBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TodoViewHolder(private val binding : CustomRowBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                // For checking the Tasks
                cbTask.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val todo = getItem(position)
                        listener.onCheckBoxClick(todo, cbTask.isChecked)
                    }
                }
            }
        }

        fun bind(todo : Todo) {
            val dateLocales = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
            val timeLocales = SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.getDefault())
            binding.apply {
                tvTaskTitle.text = todo.title
                tvTaskDate.text = dateLocales.format(todo.date)
                tvTaskTime.text = timeLocales.format(todo.time)
                cbTask.isChecked = todo.completed
                tvTaskTitle.paint.isStrikeThruText = todo.completed

                iMenus.setOnClickListener { view ->

                    val popupMenus = PopupMenu(_context, view)
                    popupMenus.inflate(R.menu.show_menu)
                    popupMenus.setOnMenuItemClickListener {
                        when(it.itemId) {
                            R.id.itEditTask -> {
                                val position = adapterPosition // this represents the position of any item in the root layout
                                // NO_POSITION means that an item is invalid and out of this list, so this is a safe check because-
                                // we don't want to call a listener on an invalid item
                                if (position != RecyclerView.NO_POSITION) {
                                    val todo = getItem(position)
                                    listener.onItemClick(todo)
                                }

                                Toast.makeText(_context, "Edit Task Button is clicked!", Toast.LENGTH_LONG).show()
                                true
                            }
                            R.id.itDeleteTask -> {
                                Toast.makeText(_context, "Delete Task Button is clicked!", Toast.LENGTH_LONG).show()
                                true
                            }
                            else -> true
                        }
                    }
                    popupMenus.show()
                    val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                    popup.isAccessible = true
                    val menu = popup.get(popupMenus)
                    menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                        .invoke(menu, true)
                }
            }
        }
    }


    interface OnItemClickListener {
        fun onItemClick(todo : Todo)
        fun onCheckBoxClick(todo: Todo, isChecked: Boolean)
    }

    // This piece of code checks between our old and changed and lists and updates the recyclerView with the latest list.
    // This also stops the recyclerView from redrawing itself after the position of an item has been changed. It even provides a nice animation.
    object DiffCallBack : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Todo, newItem: Todo) =
            oldItem == newItem
    }
}