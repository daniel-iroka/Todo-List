package com.bignerdranch.android.to_dolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO : IMPLEMENT NAVIGATION COMPONENT(NAV GRAPH) IN THIS ENTIRE PROJECT...
        // TODO : When I come back, I will continue with implementing nav graph

        // Calling in our fragment manager and telling what layout we want to host our fragment in
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container_view)

        if (currentFragment == null) {
            val fragment = TodoListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container_view, fragment)
                .commit()
        }
    }
}