package com.example.eventify.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventify.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    @Inject
    @Named("OnBoardingWelcome")
    lateinit var sharedPrefOnBoard: SharedPreferences

    @Inject
    @Named("UserLoggedIn")
    lateinit var sharedPrefLoggedIn: SharedPreferences

    val isLoading = MutableStateFlow(false)


    suspend fun loginUser(username : String, password : String) : Boolean {

        val loginChecker = viewModelScope.async {
            try {
                isLoading.update { true }
                // TODO token handle ele
                authRepository.loginUser(
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