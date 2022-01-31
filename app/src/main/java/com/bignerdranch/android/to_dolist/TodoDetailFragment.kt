package com.bignerdranch.android.to_dolist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class TodoDetailFragment: Fragment() {

    private lateinit var todo : Todo
    private lateinit var todoTitle : EditText
    private lateinit var todoDate : EditText
    private lateinit var todoTime : EditText
    private lateinit var saveButton : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todos, container, false)

        todoTitle = view.findViewById(R.id.todo_title) as EditText
        todoDate = view.findViewById(R.id.date_editText) as EditText
        todoTime = view.findViewById(R.id.time_editText) as EditText
        saveButton = view.findViewById(R.id.save_button) as Button

        return view
    }

    override fun onStart() {
        super.onStart()

        val todoTitleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // something...
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // TODO Continue from this properly
                todo.title = sequence.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                // something
            }
        }
        todoTitle.addTextChangedListener(todoTitleWatcher)

    }

    companion object {

        fun newInstance(): TodoListFragment {
            return TodoListFragment()
        }
    }
}