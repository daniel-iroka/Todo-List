package com.bignerdranch.android.to_dolist

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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

    // We specify what we want to happen when an action or menu item has been clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.todo_action_item -> {

                val fragment = TodoDetailFragment.newInstance()
                fragmentManager
                    ?.beginTransaction()
                    ?.add(R.id.container_view, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
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

    private inner class TodoViewHolder(view : View): RecyclerView.ViewHolder(view) {

        val todoTitleTextView = itemView.findViewById(R.id.todo_title) as TextView
        val todoDate = itemView.findViewById(R.id.date_editText) as TextView

    }


    private fun checkBoxStatus(todoTitle: TextView, isCheckBox: Boolean) {
        // TODO : Continue this later from philipLackner on Youtube....
        if (isCheckBox) {
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
                // TODO : When I come back, move the binder to the ViewHolder to check the issue of the Bug
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