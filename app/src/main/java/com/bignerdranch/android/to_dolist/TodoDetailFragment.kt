package com.bignerdranch.android.to_dolist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class TodoDetailFragment: Fragment() {

    private lateinit var todoTitle : TextView
    private lateinit var todoDate : EditText
    private lateinit var todoTime : EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todos, container, false)

        todoTitle = view.findViewById(R.id.todo_title) as TextView
        todoDate = view.findViewById(R.id.date_editText) as EditText
        todoTime = view.findViewById(R.id.time_editText) as EditText

        return view
    }

    override fun onStart() {
        super.onStart()

        val todoTitleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // something...
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                todoTitle.text = sequence
            }

            override fun afterTextChanged(p0: Editable?) {
                // something
            }

        }

        val todoDateWatcher = TODO("FIX LATER")
    }


}