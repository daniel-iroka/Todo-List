package com.bignerdranch.android.to_dolist.fragments.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.to_dolist.R
import com.bignerdranch.android.to_dolist.databinding.FragmentListBinding
import com.bignerdranch.android.to_dolist.databinding.FragmentUpdateBinding
import com.bignerdranch.android.to_dolist.fragments.add.SIMPLE_DATE_FORMAT
import com.bignerdranch.android.to_dolist.fragments.add.SIMPLE_TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.*

/** Our UpdateFragment. This is where we will allow the Users to be able update or edit an already existing Task. **/

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private var _binding : FragmentUpdateBinding? = null
    // This computed property just returns a ready to use
    // version of our Binding class
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflating the layout for this fragment using ViewBinding style
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)

        val dateLocales = SimpleDateFormat(SIMPLE_DATE_FORMAT, Locale.getDefault())
        val timeLocales = SimpleDateFormat(SIMPLE_TIME_FORMAT, Locale.getDefault())

        binding.updateTaskTitle.setText(args.currentTask.title)
        binding.updateDate.text = dateLocales.format(args.currentTask.date)
        binding.updateTime.text = timeLocales.format(args.currentTask.time)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}