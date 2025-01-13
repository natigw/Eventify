package com.example.eventify.presentation.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.domain.model.auth.UserData
import com.example.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @Named("LanguageChoice")
    var sharedPrefLanguage: SharedPreferences,
    private val authRepository: AuthRepository
): ViewModel() {

    val userData = MutableStateFlow<UserData?>(null)

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            val data = authRepository.getUserData()
            userData.update { data }
        }
    }
}