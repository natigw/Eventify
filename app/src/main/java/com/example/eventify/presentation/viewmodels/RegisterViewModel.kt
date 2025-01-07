package com.example.eventify.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val  authRepository: com.example.domain.repository.AuthRepository
) : ViewModel() {

    val isLoading = MutableStateFlow(false)

    @Inject
    @Named("OnBoardingWelcome")
    lateinit var sharedPrefOnBoard : SharedPreferences

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

    fun setUserEmail(userEmail : String){
        sharedPrefOnBoard.edit {
            putString("userEmail",userEmail)
        }
    }

}