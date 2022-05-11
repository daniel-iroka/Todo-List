package com.bignerdranch.android.to_dolist.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    // TODO - WHEN I COME BACK, I WILL TEST IF THIS WHOLE SHII IS EVEN GONNA WORK AND HOPEFULLY NO BUGS.
    // TODO - IF IT WORKS, THEN I PUSH THE CHANGES.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment with ViewBinding style
        _binding = FragmentListBinding.inflate(inflater, container, false)

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

    // We want to leave no trace of our Binding class Reference to avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}