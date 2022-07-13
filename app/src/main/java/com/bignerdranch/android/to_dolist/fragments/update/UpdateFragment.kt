package com.bignerdranch.android.to_dolist.fragments.update

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.to_dolist.R
import com.bignerdranch.android.to_dolist.databinding.FragmentUpdateBinding
import com.bignerdranch.android.to_dolist.fragments.add.DIALOG_DATE
import com.bignerdranch.android.to_dolist.fragments.add.DIALOG_TIME
import com.bignerdranch.android.to_dolist.fragments.add.SIMPLE_DATE_FORMAT
import com.bignerdranch.android.to_dolist.fragments.add.SIMPLE_TIME_FORMAT
import com.bignerdranch.android.to_dolist.fragments.dialogs.DatePickerFragment
import com.bignerdranch.android.to_dolist.fragments.dialogs.TimePickerFragment
import com.bignerdranch.android.to_dolist.model.Todo
import com.bignerdranch.android.to_dolist.viewmodel.TodoViewModel
import java.text.SimpleDateFormat
import java.util.*

/** Our UpdateFragment. This is where we will allow the Users to be able update or edit an already existing Task. **/

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private var _binding : FragmentUpdateBinding? = null
    // This computed property just returns a ready to use
    // version of our Binding class
    private val binding get() = _binding!!
    private lateinit var todoViewModel : TodoViewModel
    private lateinit var todo : Todo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todo = Todo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflating the layout for this fragment using ViewBinding style
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        val dateLocales = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
        val timeLocales = SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.getDefault())

        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        binding.updateTaskTitle.setText(args.currentTask.title)
        binding.updateDate.text = dateLocales.format(args.currentTask.date)
        binding.updateTime.text = timeLocales.format(args.currentTask.time)


        // will update our current _Todo
        binding.updateCheckYes.setOnClickListener {
            updateTodoInDatabase()
        }

        // update Date text field
        binding.updateDate.setOnClickListener {
            childFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) {_, bundle ->
                val result = bundle.getSerializable("bundleKey") as Date
                todo.date = result
                if(todo.date.toString().isNotEmpty()) {
                    updateDate()
                }
            }
            DatePickerFragment().show(this@UpdateFragment.childFragmentManager, DIALOG_DATE)
        }

        // update Time text field
        binding.updateTime.setOnClickListener {
            childFragmentManager.setFragmentResultListener("tRequestKey", viewLifecycleOwner) {_, bundle ->
                val result = bundle.getSerializable("tBundleKey") as Date
                todo.time = result
                if (todo.time.toString().isNotEmpty()) {
                    updateTime()
                }
            }
            TimePickerFragment().show(this@UpdateFragment.childFragmentManager, DIALOG_TIME)
        }

        return binding.root
    }

    private fun updateDate() {
        val dateLocales = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
        binding.updateDate.text = dateLocales.format(todo.date)
    }

    private fun updateTime() {
        val timeLocales = SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.getDefault())
        binding.updateTime.text = timeLocales.format(todo.time)
    }

    private fun updateTodoInDatabase() {
        val title = binding.updateTaskTitle.text.toString()
        val date = binding.updateDate.text.toString()
        val time = binding.updateTime.text.toString()

        if (inputCheck(title, date, time)) {
            val updatedTodo = Todo(args.currentTask.id, title, todo.date, todo.time)
            todoViewModel.updateTask(updatedTodo)

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(requireContext(), R.string.task_update_toast, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), R.string.no_task_add_toast, Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(title : String, date: String, time: String) : Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || time.isEmpty())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}