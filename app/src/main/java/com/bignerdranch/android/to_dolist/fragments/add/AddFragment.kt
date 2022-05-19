package com.bignerdranch.android.to_dolist.fragments.add

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.to_dolist.R
import com.bignerdranch.android.to_dolist.data.TodoViewModel
import com.bignerdranch.android.to_dolist.databinding.FragmentAddBinding
import com.bignerdranch.android.to_dolist.fragments.dialogs.DatePickerFragment
import com.bignerdranch.android.to_dolist.fragments.dialogs.TimePickerFragment
import com.bignerdranch.android.to_dolist.model.Todo
import java.text.SimpleDateFormat
import java.util.*

private const val DIALOG_DATE = "DialogDate"
private const val DIALOG_TIME = "DialogTime"
const val SIMPLE_DATE_FORMAT = "MMM, d yyyy"
const val SIMPLE_TIME_FORMAT = "H:mm"

class AddFragment : Fragment() {

    private lateinit var todoViewModel : TodoViewModel
    private var _binding : FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var todo : Todo


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

        remindersTextSpan()

        // will insert our database when clicked
        binding.abCheckYes.setOnClickListener {
            insertTodoInDatabase()
        }

        // showing our datePickerDialog
        binding.edDate.setOnClickListener {

            childFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) {_, bundle ->
                val result = bundle.getSerializable("bundleKey") as Date
                todo.date = result
                // will ONLY update the date field when it is not empty so that we don't get a preloaded Date textView
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
                // will ONLY update the time field when it is not empty so that we don't get a preloaded Time textView
                if (todo.time.toString().isNotEmpty()) {
                    updateTime()
                }

            }
           TimePickerFragment().show(this@AddFragment.childFragmentManager, DIALOG_TIME)
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

    // our reminders Text span
    // Todo - Later check the docs on this.
    private fun remindersTextSpan() {
        val spannableString = SpannableString("Set Reminders")

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(context, "Set Reminders!", Toast.LENGTH_LONG).show()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)

                ds.color = Color.BLUE
            }
        }

        spannableString.setSpan(clickableSpan, 0, 13, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)

        binding.tvReminders.text = spannableString
        binding.tvReminders.movementMethod = LinkMovementMethod.getInstance()
    }


    // This function's job will be to insert Tasks in our database
    private fun insertTodoInDatabase() {
        val title = binding.edTaskTitle.text.toString()
        val date  = binding.edDate.text.toString()
        val time = binding.edTime.text.toString()

        if (inputCheck(title, date, time)) {
            val todo = Todo(0, title)
            todoViewModel.addTodo(todo)
            // This will make a toast saying Successfully added task if we add a task
            Toast.makeText(requireContext(), R.string.task_add_toast, Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), R.string.no_task_add_toast, Toast.LENGTH_LONG).show()
        }
    }

    // This function will help us check if the texts are empty and then proceed to add them to the database
    // so that we don't add empty tasks to our database
    private fun inputCheck(title : String, date: String, time: String) : Boolean {

        // will return false if fields in TextUtils are empty and true if not
        return !(TextUtils.isEmpty(title) && TextUtils.isEmpty(date) && time.isEmpty())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}