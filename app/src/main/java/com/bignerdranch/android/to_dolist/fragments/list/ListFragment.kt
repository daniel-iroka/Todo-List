package com.bignerdranch.android.to_dolist.fragments.list

import android.app.AlertDialog
import android.os.Bundle
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

class ListFragment : Fragment() {
    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var mTodoViewModel: TodoViewModel
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment with ViewBinding style
        _binding = FragmentListBinding.inflate(inflater, container, false)

        // this tells our activity/fragment that we have a menu_item it should respond to.
        setHasOptionsMenu(true)

        val adapter = ListAdapter() // getting reference to our ListAdapter
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
        builder.setPositiveButton("Yes") {_,_->

        }
        builder.setNegativeButton("No") {_,_->}
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete only selected Tasks?")
        builder.create().show()
        // TODO - Implement the ability to delete only the selected tasks with the strikeThrough text
    }


    // We want to leave no trace of our Binding class Reference to avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}