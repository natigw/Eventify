package com.example.eventify.presentation.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    @Named("OnBoardingWelcome")
    val sharedPrefOnBoard: SharedPreferences,
    @Named("LanguageChoice")
    val sharedPrefLanguage: SharedPreferences
) : ViewModel() {

}