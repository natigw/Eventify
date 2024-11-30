package com.example.eventify.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.eventify.data.remote.api.AuthAPI
import com.example.eventify.data.remote.model.register.RequestUserRegistration
import com.example.eventify.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val  authRepository: AuthRepository
) : ViewModel() {

    val isLoading = MutableStateFlow(false)


    suspend fun registerUser(
        firstname: String,
        lastname: String,
        username: String,
        email: String,
        password: String
    ): Boolean {

        val registerChecker = viewModelScope.async {
            try {
                isLoading.update { true }
                authRepository.registerUser(
                    firstname,
                    lastname,
                    username,
                    email,
                    password
                )
                true
            }
            catch (e : Exception){
                Log.e("errorRegister",e.message.toString())
                false
            }
            finally {
                isLoading.update { false }
            }
        }

        return registerChecker.await()
    }
}