package com.bignerdranch.android.to_dolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class TodoDetailFragment: Fragment() {

    // TODO : Implement TextView later
    private lateinit var todoTitle : TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todos, container, false)

        todoTitle = view.findViewById(R.id.todo_title) as TextView

        return view
    }


}