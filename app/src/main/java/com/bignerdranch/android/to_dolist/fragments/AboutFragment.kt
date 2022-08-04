package com.bignerdranch.android.to_dolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bignerdranch.android.to_dolist.R

/** This is our 'About' Screen Fragment. This does nothing but display little info on our App and its logo. **/

class AboutFragment : Fragment() {

    // TODO - WHEN I COME BACK, I WILL IMPLEMENT THE JETPACK DATA STORE TO RETAIN THE STATE OF THE SORTED LISTS AFTER PROCESS DEATH OF OUR ACTIVITY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)
    }
}