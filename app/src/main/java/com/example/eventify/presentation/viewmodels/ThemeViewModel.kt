package com.example.eventify.presentation.viewmodels

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ThemeViewModel @Inject constructor(
    @Named("ThemeChoice")
    var sharedPrefTheme: SharedPreferences
) : ViewModel() {

    val currentTheme = sharedPrefTheme.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    var chosenTheme = currentTheme
}