package com.example.eventify.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.eventify.data.remote.api.AuthAPI
import com.example.eventify.data.remote.model.register.RequestUserRegistration
import com.example.eventify.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val  authRepository: AuthRepository
) : ViewModel() {

    fun registerUser(
        firstname: String,
        lastname: String,
        username: String,
        email: String,
        password: String
    ): Boolean {
        var registerChecker = false
        viewModelScope.launch {
            try {
                authRepository.registerUser(
                    firstname,
                    lastname,
                    username,
                    email,
                    password
                )
                registerChecker = true
            }
            catch (e : Exception){
                Log.e("error",e.message.toString())
                registerChecker = false
            }
        }
        return registerChecker
    }
}