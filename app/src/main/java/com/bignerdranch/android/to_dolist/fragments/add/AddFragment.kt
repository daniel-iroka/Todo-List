package com.bignerdranch.android.to_dolist.fragments.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.to_dolist.R
import com.bignerdranch.android.to_dolist.viewmodel.TodoViewModel
import com.bignerdranch.android.to_dolist.databinding.FragmentAddBinding
import com.bignerdranch.android.to_dolist.fragments.dialogs.DatePickerFragment
import com.bignerdranch.android.to_dolist.fragments.dialogs.TimePickerFragment
import com.bignerdranch.android.to_dolist.model.Todo
import java.text.SimpleDateFormat
import java.util.*

const val DIALOG_DATE = "DialogDate"
const val DIALOG_TIME = "DialogTime"
const val SIMPLE_DATE_FORMAT = "MMM, d yyyy"
const val SIMPLE_TIME_FORMAT = "H:mm"


class AddFragment : Fragment() {

    private lateinit var todoViewModel : TodoViewModel
    private var _binding : FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var todo : Todo


    // TODO - WHEN I COME BACK, FIRSTLY, I WILL TRY TO FIX AND CHECK THE REASON WHY THE DIALOG BOX COLOR ISN'T CHANGING
    // TODO - WHEN I COME BACK, I MAY ALSO TRY TO INCREASE THE SIZE OF THE EDITTEXTS TO SEE IF IT WILL LOOK BETTER.
    // TODO - ALSO WHEN I COME BACK, I WILL CONTINUE EXPERIMENTING WITH MORE COLOURS

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
                // passing the result of the user selected date directly to the _Todo class instead. Will do the same for also the time.
                todo.date = result
                // will ONLY update the date field when it is not empty so that we don't get a preloaded Date textView. Will do the same for also the time.
                if(todo.date.toString().isNotEmpty()) {
                    updateDate()
                }
            }

            DatePickerFragment().show(this@AddFragment.childFragmentManager, DIALOG_DATE)
        }

        // showing our timePickerDialog
        binding.edTime.setOnClickListener {

            childFragmentManager.setFragmentResultListener("tRequestKey", viewLifecycleOwner) {_, bundle ->
                val result = bundle.getSerializable("tBundleKey") as Date
                todo.time = result
                if (todo.time.toString().isNotEmpty()) {
                    updateTime()
                }

            }
           TimePickerFragment().show(this@AddFragment.childFragmentManager, DIALOG_TIME)
        }

        // todo - ALso fix this thing.
        if (binding.edDate.text.toString().isNotEmpty()) {
            binding.iClearSearch.visibility = View.VISIBLE
        }

        binding.iClearSearch.setOnClickListener {
            binding.edDate.text = ""
        }

        if (binding.edTime.text.toString().isNotEmpty()) {
            binding.iClearSearch2.visibility = View.VISIBLE
        }

        binding.iClearSearch2.setOnClickListener {
            binding.edTime.text = ""
        }
        return binding.root
    }


    // function to update Date
    private fun updateDate() {
        val dateLocales = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
        binding.edDate.text = dateLocales.format(todo.date)
    }

    // function to update Time
    private fun updateTime() {
        val timeLocales = SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.getDefault())
        binding.edTime.text = timeLocales.format(todo.time)
    }

    // TODO - THIS IS A LATER TIME TODO BUT I MAY EXPERIMENT WITH "GONE" INSTEAD OF INVISIBLE TO SHOW THE SET ALARM FOR EACH TASK IN THE RECYCLERVIEW


    // This function's job will be to insert Tasks in our database
    private fun insertTodoInDatabase() {
        val title = binding.edTaskTitle.text.toString()
        val date  = binding.edDate.text.toString()
        val time = binding.edTime.text.toString()

        if (inputCheck(title, date, time)) {
            val todo = Todo(0, title, todo.date, todo.time)
            todoViewModel.addTodo(todo)
            // This will make a toast saying Successfully added task if we add a task
            Toast.makeText(requireContext(), R.string.task_add_toast, Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), R.string.no_task_add_toast, Toast.LENGTH_LONG).show()
        }
    }

    // This function will help us check if the texts are empty and then proceed to add them to the database
    // so that we do not add empty tasks to our database
    private fun inputCheck(title : String, date: String, time: String) : Boolean {

        // will return false if fields in TextUtils are empty and true if not
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || time.isEmpty())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
