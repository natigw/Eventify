package com.example.eventify.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
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
    val authRepository: AuthRepository
): ViewModel() {

    @Inject
    @Named("OnBoardingWelcome")
    lateinit var sharedPrefOnBoard: SharedPreferences


    @Inject
    @Named("UserTokens")
    lateinit var sharedPrefUserTokens: SharedPreferences

    val isLoading = MutableStateFlow(false)

    val errorMessagesState = MutableStateFlow<String?>(null)
    suspend fun loginUser(username : String, password : String) : Boolean {

        val loginChecker = viewModelScope.async {
            try {
                isLoading.update { true }
                val response = authRepository.loginUser(
                    username = username,
                    password = password
                )
                sharedPrefUserTokens.edit {
                    putString("access_token",response.accessToken)
                    putString("refresh_token",response.refreshToken)
                    putString("token_type",response.tokenType)
                }
                true
            }
            catch (e : Exception){
                errorMessagesState.update { e.message }
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