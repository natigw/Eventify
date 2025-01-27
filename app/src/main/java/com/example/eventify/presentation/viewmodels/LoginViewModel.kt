package com.example.eventify.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import com.example.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LoginViewModel @Inject constructor(
    @Named("OnBoardingWelcome")
    var sharedPrefOnBoard: SharedPreferences,
    @Named("UserTokens")
    var sharedPrefUserTokens: SharedPreferences,
    val authRepository: AuthRepository
): ViewModel() {

    val isLoading = MutableStateFlow<Boolean?>(null)
    val isLoadingGoogle = MutableStateFlow<Boolean?>(null)
    private val errorMessagesState = MutableStateFlow<String?>(null)

    suspend fun loginUser(username : String, password : String) : Boolean {
        return withContext(Dispatchers.IO){
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
    }

    suspend fun linkGoogleAccount() : Boolean{
        return withContext(Dispatchers.IO) {
            try {
                isLoadingGoogle.update { true }
                val response = authRepository.signInWithGoogle()
                sharedPrefUserTokens.edit {
                    putString("access_token", response.accessToken)
                    putString("refresh_token", response.refreshToken)
                    putString("token_type", response.tokenType)
                }

                true
            }
            catch (e : Exception){
                Log.e("mainException",e.message.toString())
                e.printStackTrace()
                false
            }
            finally {
                isLoadingGoogle.update { false }
            }
        }
    }
}