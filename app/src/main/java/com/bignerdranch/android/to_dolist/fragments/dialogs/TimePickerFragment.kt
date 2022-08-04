package com.bignerdranch.android.to_dolist.fragments.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar
import java.util.GregorianCalendar

class TimePickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val timeListener =  TimePickerDialog.OnTimeSetListener {
                _ : TimePicker, hourOfDay, minute ->

            // NOTE! : As to regards to this, this is the TimePicker fragment so we only need "Time" calender instance, so my only guess as to why we added
            // year, day and month is that these other calender values are required in the Gregorian calender class below, hence we create and add them.
            val calender = Calendar.getInstance()
            val year = calender.get(Calendar.YEAR)
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val month = calender.get(Calendar.MONTH)

            val mainTimeResult = GregorianCalendar(year, day, month, hourOfDay, minute).time

            val result = Bundle().apply {
                putSerializable("tBundleKey", mainTimeResult)
            }
            parentFragmentManager.setFragmentResult("tRequestKey", result)
        }

        val calender = Calendar.getInstance()
        val hour = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            timeListener,
            hour,
            minute,
            false
        )
    }
}