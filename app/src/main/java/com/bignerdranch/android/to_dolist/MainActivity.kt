package com.bignerdranch.android.to_dolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.bignerdranch.android.to_dolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBarWithNavController(findNavController(R.id.fragmentContainerView))
    }

    // This function will enable us to be able to use the Default back arrow button in our nav_graph action bar
    override fun onSupportNavigateUp(): Boolean {
        val navigateUp = findNavController(R.id.fragmentContainerView)
        return navigateUp.navigateUp() || super.onSupportNavigateUp()
    }
}
