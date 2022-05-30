package com.bignerdranch.android.to_dolist.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
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
            }

            R.id.del_all_tasks -> {
                deleteAllTasks()
            }

            else -> super.onOptionsItemSelected(item)
        }


    }

    private fun deleteAllTasks(): Boolean {

    }

    private fun deleteSelectedUsers(): Boolean {

    }


    // We want to leave no trace of our Binding class Reference to avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}