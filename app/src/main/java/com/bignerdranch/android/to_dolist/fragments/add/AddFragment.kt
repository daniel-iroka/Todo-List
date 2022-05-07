package com.bignerdranch.android.to_dolist.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bignerdranch.android.to_dolist.R
import com.bignerdranch.android.to_dolist.data.TodoViewModel
import com.bignerdranch.android.to_dolist.databinding.FragmentAddBinding
import com.bignerdranch.android.to_dolist.fragments.dialogs.DatePickerFragment
import com.bignerdranch.android.to_dolist.fragments.dialogs.TimePickerFragment
import com.bignerdranch.android.to_dolist.model.Todo
import java.util.*

private const val DIALOG_DATE = "DialogDate"
private const val DIALOG_TIME = "DialogTime"

class AddFragment : Fragment() {

    private lateinit var todoViewModel : TodoViewModel
    private lateinit var todo : Todo
    private var _binding : FragmentAddBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todo = Todo()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]


        // will insert our database when clicked
        binding.abCheckYes.setOnClickListener {
            insertTodoInDatabase()
        }

        // showing our datePickerDialog
        binding.edDate.setOnClickListener {

            childFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) {_, bundle ->
                val result = bundle.getSerializable("bundleKey") as Date
                todo.date = result
            }

            val showDate = DatePickerFragment()
            showDate.show(this@AddFragment.childFragmentManager, DIALOG_DATE)
        }

        // showing our timePickerDialog
        binding.edTime.setOnClickListener {
            // TODO - RUN THIS LATER BECAUSE I HAVEN'T TESTED IT.
            childFragmentManager.setFragmentResultListener("tRequestKey", viewLifecycleOwner) {_, bundle ->
                val result = bundle.getSerializable("tBundleKey") as Date
                todo.time = result
            }

            val showTime = TimePickerFragment()
            showTime.show(this@AddFragment.childFragmentManager, DIALOG_TIME)
        }

        return binding.root
    }


    // This function's job will be to insert Tasks in our database
    private fun insertTodoInDatabase() {
        val title = binding.edTaskTitle.text.toString()
        val date  = binding.edDate
        val time = binding.edTime

        if (inputCheck(title, date, time)) {
            val todo = Todo(0, title)
            todoViewModel.addTodo(todo)
            // This will make a toast saying Successfully added task if we add a task
            Toast.makeText(requireContext(), R.string.task_add_toast, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), R.string.no_task_add_toast, Toast.LENGTH_LONG).show()
        }
    }

    // This function will help us check if the texts are empty and then proceed to add them to the database
    // so that we don't add empty tasks to our database
    private fun inputCheck(title : String, date: EditText, time: EditText) : Boolean {

        // will return false if fields in TextUtils are empty and true if not
        // TODO - There is also a bug here in which I will attend to later
        return (TextUtils.isEmpty(title) && TextUtils.isEmpty(date.toString()) && time.toString().isEmpty())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}