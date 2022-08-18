package com.bignerdranch.android.to_dolist.fragments.list

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
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

        @SuppressLint("DiscouragedPrivateApi")
        fun bind(todo : Todo) {
            val dateLocales = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
            val timeLocales = SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.getDefault())
            binding.apply {
                tvTaskTitle.text = todo.title
                tvTaskDate.text = dateLocales.format(todo.date)
                tvTaskTime.text = timeLocales.format(todo.time)
                cbTask.isChecked = todo.completed
                tvTaskTitle.paint.isStrikeThruText = todo.completed
                tvResultsReminder.isVisible = todo.important
                reminderIcon.isVisible = todo.important

                // Will only show the resultsReminder if important is true.
                if (todo.important) {
                    tvResultsReminder.text = DateUtils.getRelativeDateTimeString(_context, todo.reminder.time, DateUtils.DAY_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0)
                }

                // will change the color of the text if the time is Overdue.
                val date = Date()
                if (todo.reminder.time < date.time) {
                    tvResultsReminder.setTextColor(ContextCompat.getColor(_context, R.color.red))
                    reminderIcon.setColorFilter(_context.resources.getColor(R.color.red))
                }

                // Implementing our PopupMenus to Edit and Delete a Task.
                iMenus.setOnClickListener { view ->

                    val popupMenus = PopupMenu(_context, view)
                    popupMenus.inflate(R.menu.show_menu)
                    popupMenus.setOnMenuItemClickListener {
                        when(it.itemId) {
                            R.id.itEditTask -> {
                                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(todo)
                                itemView.findNavController().navigate(action)
                                true
                            }

                            R.id.itDeleteTask -> {
                                val position = adapterPosition// this represents the position of any item in the root layout
                                // NO_POSITION means that an item is invalid and out of this list, so this is a safe check because-
                                // we don't want to call a listener on an invalid item
                                if (position != RecyclerView.NO_POSITION) {
                                    val curTodo = getItem(position)
                                    listener.onItemDelete(curTodo)
                                }
                                Toast.makeText(_context, "Task has been deleted.", Toast.LENGTH_LONG).show()
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

    /** IMPORTANT NOTE! - onItemClick andonCheckBox click both do the same thing but the first is called when the task is clicked and
     **                   and the second is called when the checkBox itself is clicked. This might be considered redundant but I separated them to avoid confusion.**/

    interface OnItemClickListener {
        fun onCheckBoxClick(todo: Todo, isChecked: Boolean)
        fun onItemDelete(todo : Todo)
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