package com.bignerdranch.android.to_dolist.fragments.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bignerdranch.android.to_dolist.R

// TODO - WHEN I COME BACK, I WILL TRY AND FIX THE BUG THAT ISN'T ALLOWING ME TO ADD ARGUMENTS TO MY FRAGMENT

class UpdateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false)
    }

}