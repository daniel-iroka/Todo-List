package com.bignerdranch.android.to_dolist

import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


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
        todo = Todo()
        setHasOptionsMenu(true) // telling our fragment manager to respond to a call from onCreateOptionsMenu..
    }


    // this is called when we have an action item we want to use in our layout
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.todo_item_list, menu)
    }

    // We specify what we want to happen when an action or menu item has been clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
        TODO("Do this later after setting up database")
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

    private inner class TodoViewHolder(view : View): RecyclerView.ViewHolder(view) {

        val todoTitleTextView = itemView.findViewById(R.id.todo_title) as TextView
        val todoDate = itemView.findViewById(R.id.date_editText) as TextView
        var todoCheckBox = itemView.findViewById(R.id.todo_checkBox) as CheckBox
    }


    private fun checkBoxStatus() {
        TODO("LATER IMPLEMENT THE STRIKE THROUGH FEATURE")
        val todo = Todo()
        if (todo.todoCheckBox == true) {
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
            holder.apply {
                todoTitleTextView.text = todo.title
                todoDate.text = todo.date.toString()
            }
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