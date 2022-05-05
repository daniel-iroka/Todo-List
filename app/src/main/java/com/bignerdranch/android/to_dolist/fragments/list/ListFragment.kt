package com.bignerdranch.android.to_dolist.fragments.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.to_dolist.databinding.FragmentListBinding
import com.bignerdranch.android.to_dolist.R


class ListFragment : Fragment() {

    // TODO - WHEN I COME BACK, I WIL UPDATE THIS APP BECAUSE IT IS DISPLAY/ING UNEXPLAINABLE ERRORS AND IT IS NOT INSTALLING.
    // TODO - I'M NOT SURE IF THIS WILL FIX IT BUT I WILL START FROM THERE AT LEAST

    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment with ViewBinding style
        _binding = FragmentListBinding.inflate(inflater, container, false)

        binding.fbAdd.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
        
        val view = binding.root

        return view
    }

    // We want to leave no trace of our Binding class Reference to avoid memory leaks
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}