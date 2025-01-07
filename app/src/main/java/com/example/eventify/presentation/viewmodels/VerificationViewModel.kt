package com.example.eventify.presentation.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class VerificationViewModel @Inject constructor(
    val authRepository: AuthRepository
) : ViewModel() {

    @Inject
    @Named("OnBoardingWelcome")
    lateinit var sharedPrefOnBoard : SharedPreferences

    val isLoading = MutableStateFlow<Boolean?>(null)
    val resendVerificationState = MutableStateFlow<Boolean?>(null)

    suspend fun isUserVerified(userEmail: String) : Boolean{
        return withContext(Dispatchers.IO){
            try {
                isLoading.update { true  }
                authRepository.isUserVerified(userEmail)
            }
            catch (_:Exception){
                false
            }
            finally {
                isLoading.update { false }
            }
        }
    }

    suspend fun resendVerification(userEmail: String): Boolean{
        return withContext(Dispatchers.IO){
            try {
                resendVerificationState.update { true }
                authRepository.resendVerification(userEmail)
            }
            catch (_:Exception){
                false
            }
            finally {
                resendVerificationState.update { false }
            }
        }
    }

    fun getUserEmail(): String{
        val userEmail = sharedPrefOnBoard.getString("userEmail","")
        return userEmail!!
    }

}