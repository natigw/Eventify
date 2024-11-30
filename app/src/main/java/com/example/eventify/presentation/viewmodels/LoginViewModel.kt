package com.example.eventify.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventify.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    val isLoading = MutableStateFlow(false)


    suspend fun loginUser(grantType : String, username : String, password : String) : Boolean {

        val loginChecker = viewModelScope.async {
            try {
                isLoading.update { true }
                // TODO token handle ele
                authRepository.loginUser(
                    grantType = grantType,
                    username = username,
                    password = password
                )
                true
            }
            catch (e : Exception){
                Log.e("errorLogin",e.message.toString())
                 false
            }
            finally {
                isLoading.update { false }
            }

        }
        return loginChecker.await()
    }

}