package com.bignerdranch.android.to_dolist.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.to_dolist.databinding.FragmentListBinding
import com.bignerdranch.android.to_dolist.R
import com.bignerdranch.android.to_dolist.data.TodoViewModel
import com.bignerdranch.android.to_dolist.model.Todo

private const val TAG = "ListFragment"

class ListFragment : Fragment() {
    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mTodoViewModel: TodoViewModel
    private lateinit var recyclerView: RecyclerView
    private val adapter = ListAdapter()  // getting reference to our ListAdapter
    private lateinit var selectedTodos : List<Todo>

    // second method
//    var selectedTodos = arrayOf<Todo>()


    // TODO - WHEN I COME BACK, I WILL SEE IF I CAN DO THE IMPLEMENTATION HERE IN THE LIST FRAGMENT

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment with ViewBinding style
        _binding = FragmentListBinding.inflate(inflater, container, false)

        // this tells our activity/fragment that we have a menu_item it should respond to.
        setHasOptionsMenu(true)

        recyclerView = binding.recyclerViewTodo
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        /**
         *  updates our recyclerView with the new "observed" changes in our database through our adapter
         */
        // TodoViewModel
        mTodoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        mTodoViewModel.readAllData.observe(viewLifecycleOwner) { todos ->
            adapter.setData(todos)
            selectedTodos = todos
            // TODO - WHEN I COME BACK, I WILL OBTAIN THE TODO FROM HERE.
        }

        // Add Task Button
        binding.fbAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId)  {
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
    private fun deleteSelectedUsers() {
        val builder = AlertDialog.Builder(requireContext())
        val todo = emptyList<Todo>()
        val finishedTodos = selectedTodos.takeWhile {  it.todoCheckBox }
        builder.setPositiveButton("Yes") {_,_->

            // second method
//            mTodoViewModel.deleteSelectedTasks(selectedTodos)

//            selectedTodos.forEach { todo ->
//                mTodoViewModel.deleteSelectedTasks(todo.id)
//            }
        }
        builder.setNegativeButton("No") {_,_->}
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete only selected Tasks?")
        builder.create().show()
        Log.d(TAG, "Our todos $selectedTodos and ${finishedTodos.size}")
    }


    // We want to leave no trace of our Binding class Reference to avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}