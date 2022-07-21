package com.bignerdranch.android.to_dolist.fragments.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

/** NOTE! : We will host our DatePicker in a Dialog fragment. A dialog fragment is used to host Dialogs. **/

class DatePickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dateListener  = DatePickerDialog.OnDateSetListener {
                _: DatePicker, year : Int, month : Int, dayOfMonth : Int ->


            val resultDate : Date = GregorianCalendar(year, month, dayOfMonth).time

            val result = Bundle().apply{
                putSerializable("bundleKey", resultDate)
            }

            // we use parentFragmentManager because our AddFragment is hosting this fragment
            parentFragmentManager.setFragmentResult("requestKey", result)
        }

        val calender = Calendar.getInstance()

        val initialYear = calender.get(Calendar.YEAR)
        val initialMonth = calender.get(Calendar.MONTH)
        val initialDay = calender.get(Calendar.DAY_OF_MONTH)

        // The DatePickerDialog prompts the user to select a date or time for each _Todo or Task
        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay,
        )
    }

}