package com.bignerdranch.android.to_dolist.fragments.add

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.to_dolist.*
import com.bignerdranch.android.to_dolist.viewmodel.TodoViewModel
import com.bignerdranch.android.to_dolist.databinding.FragmentAddBinding
import com.bignerdranch.android.to_dolist.fragments.dialogs.DatePickerFragment
import com.bignerdranch.android.to_dolist.fragments.dialogs.TimePickerFragment
import com.bignerdranch.android.to_dolist.model.Todo
import com.bignerdranch.android.to_dolist.utils.*
import java.text.SimpleDateFormat
import java.util.*

const val DIALOG_DATE = "DialogDate"
const val DIALOG_TIME = "DialogTime"
const val SIMPLE_DATE_FORMAT = "MMM, d yyyy"
const val SIMPLE_TIME_FORMAT = "H:mm a"

class AddFragment : Fragment() {

    // TODO - WHEN I COME BACK, I WILL CONTINUE IN THE IMPLEMENTING OF THIS ALARM REMINDER AND SEE HOW TO IMPLEMENT IT PROPERLY TO FIT MY APP.

    private lateinit var todoViewModel : TodoViewModel
    private var _binding : FragmentAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var todo : Todo
    private var setTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todo = Todo()

        createNotificationsChannel()
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

            scheduleNotification()
        }

        // Our clear search buttons
        binding.iClearSearch2.setOnClickListener {
            binding.edDate.text = ""
        }

        binding.iClearSearch.setOnClickListener {
            binding.edTime.text = ""
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

        binding.btnReminder.setOnClickListener {
            pickDateAndTime()
        }
        return binding.root
    }

    private fun scheduleNotification() {
        val title = binding.edTaskTitle.text.toString()
        val message = binding.btnReminder.text.toString()
        val intent = Intent(requireContext(), Notifications::class.java).apply {
            putExtra(TITLE_EXTRA, title)
            putExtra(MESSAGE_EXTRA, message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext().applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            setTime,
            pendingIntent
        )

//        showAlert(setTime, title, message)
    }

    // todo - IMPORTANT NOTE! : I WILL REMOVE THIS WHEN I COME BACK. THERE IS NO NEED FOR THIS.
//    private fun showAlert(time: Long, title: String, message: String) {
//        val date = Date(time)
//        val dateFormat = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
//        val timeFormat = SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.getDefault())
//
//        AlertDialog.Builder(requireContext())
//            .setTitle("Notification Scheduled")
//            .setMessage(
//                "Title:" + title +
//                "\nMessage: " + message +
//                "\nAt:" + dateFormat.format(date) + "  " + timeFormat.format(date)
//            ).setPositiveButton("Okay"){_,_->}
//            .show()
//    }

    private fun pickDateAndTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(requireContext(), { _, year, month, day ->
            TimePickerDialog(requireContext(), { _, hour, minute ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day, hour, minute)
                setTime = pickedDateTime.timeInMillis

            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }

    // We create a Notifications channel and register it to our system. We must do this before post our Notifications.
    private fun createNotificationsChannel() {
        val name = "Notification Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = desc

        // Registering the channel with the system
        val notificationManger = requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManger.createNotificationChannel(channel)
    }


    // function to update Date
    private fun updateDate() {
        val dateLocales = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
        binding.edDate.text = todo.date.toString()
    }

    // function to update Time
    private fun updateTime() {
        val timeLocales = SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.getDefault())
        binding.edTime.text = timeLocales.format(todo.time)
    }


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
