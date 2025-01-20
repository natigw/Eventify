package com.example.eventify.presentation.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LanguageViewModel @Inject constructor(
    @Named("LanguageChoice")
    var sharedPrefLanguage: SharedPreferences
) : ViewModel() {

    val currentLanguage = sharedPrefLanguage.getString("language", "en")
    var chosenLanguage = currentLanguage!!
}