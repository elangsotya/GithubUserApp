package com.example.submissionaplikasigithubuser.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissionaplikasigithubuser.preferences.themePreferences

class ViewModelFactory(private val pref: themePreferences) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(themeViewModel::class.java)) {
            return themeViewModel(pref) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        }
        throw IllegalAccessException("Unknown ViewModel class: " + modelClass.name)
    }
}