package com.bignerdranch.android.to_dolist

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.to_dolist.model.Todo

private const val TAG = "TodoListFragment"

class TodoListFragment : Fragment() {
    private var todo = Todo()
    private lateinit var todoRecyclerView: RecyclerView
    private var adapter: TodoAdapter? = null

    // Created an instance of our ViewModel
    private val todoListViewModel : TodoViewModel by lazy {
        ViewModelProvider(this).get(TodoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "We got some todos here: ${todoListViewModel.todos.size}")
        todo = Todo()
        setHasOptionsMenu(true) // telling our fragment manager to respond to a call from onCreateOptionsMenu..
    }


    // this is called when we have an action item we want to use in our layout
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.todo_item_list, menu)
    }


    private fun switchFragments() {
        val fragment = TodoDetailFragment()
        fragmentManager
            ?.beginTransaction()
            ?.add(R.id.container_view, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }


    // We specify what we want to happen when an action or menu item has been clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.todo_action_item -> {
                // Todo : Fix this bug........
                switchFragments()
                updateTodoUI()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.todo_item_list, container, false)

        todoRecyclerView = view.findViewById(R.id.todo_recyclerView) as RecyclerView

        // we define our RecyclerView to be displayed in a linear mode through our layoutManager
        todoRecyclerView.layoutManager = LinearLayoutManager(context)
        todoRecyclerView.adapter = adapter

        updateTodoUI()

        return view
    }


    // function to strike through our text when checked
    private fun checkBoxStatus(todoTitle: TextView, isCheckBox: Boolean) {
        if (isCheckBox) {
            todoTitle.paintFlags = todoTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            todoTitle.paintFlags = todoTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }


    private inner class TodoViewHolder(view : View): RecyclerView.ViewHolder(view) {

        private lateinit var todo : Todo
        val todoTitleTextView = itemView.findViewById(R.id.todo_title) as TextView
        val todoDate = itemView.findViewById(R.id.date_editText) as TextView
        val checkBox = itemView.findViewById(R.id.todo_checkBox) as CheckBox
        

        // function to bind our views
        // todo Test this mofoko later....
        fun bind(todo : Todo) {
            this.todo = todo
            todoTitleTextView.text = this.todo.title
            todoDate.text = this.todo.date.toString()
            checkBox.isChecked = this.todo.todoCheckBox

            checkBoxStatus(todoTitleTextView, todo.todoCheckBox)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                checkBoxStatus(todoTitleTextView, isChecked)
                checkBox.isChecked = todo.todoCheckBox
            }
        }
    }


    // Our TodoAdapter which sets up the data to be displayed in our RecyclerView through its ViewHolder
    private inner class TodoAdapter(val todos: List<Todo>): RecyclerView.Adapter<TodoViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
            val view = layoutInflater.inflate(R.layout.fragment_todos_list, parent, false)
            return TodoViewHolder(view)
        }

        override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
            val todo = todos[position]
            holder.bind(todo)
        }

        override fun getItemCount() = todos.size // get size of todos
    }


    private fun updateTodoUI() {
        val todo = todoListViewModel.todos
        adapter = TodoAdapter(todo)
        todoRecyclerView.adapter = adapter
    }

    companion object {

        fun newInstance(): TodoListFragment {
            return TodoListFragment()
        }
    }
}