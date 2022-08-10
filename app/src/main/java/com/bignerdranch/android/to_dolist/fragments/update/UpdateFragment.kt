package com.bignerdranch.android.to_dolist.fragments.update

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
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
import com.bignerdranch.android.to_dolist.utils.*
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
    private var setDateTime : Long = 0
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        todo = Todo()

        createNotificationsChannel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflating the layout for this fragment using ViewBinding style
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        val dateLocales = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
        val timeLocales = SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.getDefault())

        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        binding.updateTaskTitle.setText(args.currentTask.title)
        binding.updateDate.text = dateLocales.format(args.currentTask.date)
        binding.updateTime.text = timeLocales.format(args.currentTask.time)

        // will only show the task in the updateReminder if the Task is Important(Has a Reminder)
        if (args.currentTask.important) {
            binding.updateReminder.text = DateUtils.getRelativeDateTimeString(context, args.currentTask.reminder.time, DateUtils.DAY_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0)
            binding.iClearReminder2.visibility = View.VISIBLE
        }

        // will update our current _Todo
        binding.updateCheckYes.setOnClickListener {
            updateTodoInDatabase()
            if (setDateTime > 1) {
                scheduleNotification()
            }
        }

        // Our clear search buttons
        binding.iClearDate2.setOnClickListener {
            binding.updateDate.text = ""
        }

        binding.iClearTime2.setOnClickListener {
            binding.updateTime.text = ""
        }

        // update Date text field
        binding.updateDate.setOnClickListener {

            childFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner) {_, bundle ->
                val result = bundle.getSerializable("bundleKey") as Date
                args.currentTask.date = result
                updateDate()
            }
            DatePickerFragment().show(this@UpdateFragment.childFragmentManager, DIALOG_DATE)
        }

        // update Time text field
        binding.updateTime.setOnClickListener {

            childFragmentManager.setFragmentResultListener("tRequestKey", viewLifecycleOwner) {_, bundle ->
                val result = bundle.getSerializable("tBundleKey") as Date
                args.currentTask.time = result
                updateTime()
            }
            TimePickerFragment().show(this@UpdateFragment.childFragmentManager, DIALOG_TIME)
        }

        // Our reminder button. I will run this later when I start my App
        binding.updateReminder.setOnClickListener {

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
                    // This 'time' will be solely for being displayed in our Set Reminder textView
                    val setDateTimeForTextView = pickedDateTime.timeInMillis
                    setDateTime = pickedDateTime.timeInMillis
                    args.currentTask.reminder = Date(setDateTimeForTextView)
                    args.currentTask.important = true
                    updateDateTime()

                }, startHour, startMinute, false).show()
            }, startYear, startMonth, startDay).show()
        }

        // this will clear our reminder selection
        binding.iClearReminder2.apply {
            setOnClickListener {
                binding.updateReminder.setText(R.string.set_reminder)
                visibility = View.INVISIBLE
                setDateTime = 0
                args.currentTask.important = false
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_update, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.updateDeleteTask) {
            deleteTask()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteTask() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") {_, _ ->
            todoViewModel.deleteTask(args.currentTask)
            Toast.makeText(requireContext(), "Successfully deleted ${args.currentTask.title}", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") {_, _-> }
        builder.setTitle("Confirm Deletion")
        builder.setMessage("Are you sure you want to delete this Task?")
        builder.show()
    }

    private fun scheduleNotification() {
        val title = binding.updateTaskTitle.text.toString()
        val intent = Intent(requireContext(), Notifications::class.java).apply {
            putExtra(TITLE_EXTRA, title)
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
            setDateTime,
            pendingIntent
        )
    }

    // We create a Notifications channel and register it to our system. We must do this before post our Notifications.
    private fun createNotificationsChannel() {
        val name = "Notification Channel"
        val desc = "A Description of the Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)
        channel.description = desc

        // Registering the channel with the system
        val notificationManger = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManger.createNotificationChannel(channel)
    }

    // function to update DateTime
    private fun updateDateTime() {
        binding.iClearReminder2.visibility = View.VISIBLE
        binding.updateReminder.text = DateUtils.getRelativeDateTimeString(context, args.currentTask.reminder.time, DateUtils.DAY_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0)
    }

    private fun updateDate() {
        val dateLocales = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
        binding.updateDate.text = dateLocales.format(args.currentTask.date)
    }

    private fun updateTime() {
        val timeLocales = SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.getDefault())
        binding.updateTime.text = timeLocales.format(args.currentTask.time)
    }

    private fun updateTodoInDatabase() {
        val title = binding.updateTaskTitle.text.toString()
        val date = binding.updateDate.text.toString()
        val time = binding.updateTime.text.toString()
        val reminder = binding.updateReminder.text.toString()

        if (inputCheck(title, date, time, reminder)) {
            val updatedTodo = Todo(args.currentTask.id, title, args.currentTask.date, args.currentTask.time, args.currentTask.reminder, args.currentTask.important)
            todoViewModel.updateTask(updatedTodo)

            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            Toast.makeText(requireContext(), R.string.task_update_toast, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), R.string.no_task_add_toast, Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(title : String, date: String, time: String, reminder : String, important : Boolean = false) : Boolean {
        return !(TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || time.isEmpty() || reminder != "Set reminder" && important)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}