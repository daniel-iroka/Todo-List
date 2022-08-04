package com.bignerdranch.android.to_dolist.utils

import androidx.appcompat.widget.SearchView

/**
 *  This inline function is responsible for handling the listeners on our searchQuery. Using this method is just good practice and improves efficiency
 */

inline fun SearchView.onQueryTextChanged(crossinline listener : (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            // will return an empty string if the newText is null instead of null
            listener(newText.orEmpty())
            return true
        }
    })
}
