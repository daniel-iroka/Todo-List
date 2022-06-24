package com.bignerdranch.android.to_dolist.fragments.list

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.to_dolist.databinding.FragmentListBinding
import com.bignerdranch.android.to_dolist.R
import com.bignerdranch.android.to_dolist.data.SortOrder
import com.bignerdranch.android.to_dolist.data.TodoViewModel
import com.bignerdranch.android.to_dolist.model.Todo
import com.bignerdranch.android.to_dolist.utils.onQueryTextChanged

private const val TAG = "ListFragment"

class ListFragment : Fragment() {
    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!
    lateinit var mTodoViewModel: TodoViewModel
    private lateinit var recyclerView: RecyclerView
    private val adapter = ListAdapter()  // getting reference to our ListAdapter
    private var todosList = emptyList<Todo>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment with ViewBinding style
        _binding = FragmentListBinding.inflate(inflater, container, false)

        // this tells our activity/fragment that we have a menu_item it should respond to it.
        setHasOptionsMenu(true)

        recyclerView = binding.recyclerViewTodo
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        /**
         *  updates our recyclerView with the new "observed" changes in our database through our adapter
         */
        // TodoViewModel
        mTodoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        mTodoViewModel.tasks.observe(viewLifecycleOwner) { todos ->
            adapter.setData(todos)
            todosList = todos
        }

        // Add Task Button
        binding.fbAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_list, menu)

        val search = menu.findItem(R.id.todo_search)
        val searchView = search.actionView as SearchView

        searchView.onQueryTextChanged { querySearch ->
            mTodoViewModel.searchQuery.value = querySearch
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId)  {
            R.id.sort_by_name -> {
                mTodoViewModel.sortOrder.value = SortOrder.BY_NAME
                true
            }

            R.id.sort_by_date -> {
                mTodoViewModel.sortOrder.value = SortOrder.BY_DATE
                true
            }

            R.id.todo_hide_completed -> {
                item.isChecked = !item.isChecked
                mTodoViewModel.hideCompleted.value = item.isChecked
                true
            }

            R.id.del_selected_tasks -> {
                deleteSelectedUsers()
                 true
            }

            R.id.del_all_tasks -> {
                deleteAllTasks()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // function to delete all of our Tasks
    private fun deleteAllTasks() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_,_->
            mTodoViewModel.deleteAllTasks()
            Toast.makeText(requireContext(), "All tasks have been successfully deleted!", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No") {_,_-> }
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete all Tasks?")
        builder.create().show()
    }

    // function to delete only selected Tasks
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteSelectedUsers() {
        val builder = AlertDialog.Builder(requireContext())
        // Our todos that have been marked completed by the checkBox
        val finishedTodos = todosList.filter { it.todoCheckBox }

        builder.setPositiveButton("Yes") {_,_->
            finishedTodos.forEach { todos ->
                mTodoViewModel.deleteSelectedTasks(todos.id.toLong())
            }
            Toast.makeText(requireContext(), "Selected tasks successfully deleted!", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("No") {_,_-> }
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete only selected Tasks?")
        builder.create().show()
        Log.i(TAG , "Our todos list size is ${finishedTodos.size}")
    }


    // We want to leave no trace of our Binding class Reference to avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}