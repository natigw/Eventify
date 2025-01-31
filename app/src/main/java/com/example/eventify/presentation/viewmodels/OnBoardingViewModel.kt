package com.example.eventify.presentation.viewmodels

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.AuthRepository
import com.example.eventify.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    @Named("UserTokens")
    val sharedPrefToken: SharedPreferences,
    val authRepository: AuthRepository
): ViewModel() {

    fun checkRefreshTokenIsValid(refreshToken : String) : Boolean{
        return try {
            viewModelScope.launch {
                NetworkUtils.handleInvalidAccessToken()
            }
            true
        }
        catch (e:Exception){

            false
        }
    }

    fun checkUser() : String?{
        return sharedPrefToken.getString("access_token",null)
    }

    fun clearTokens(){
        sharedPrefToken.edit{
            clear()
        }
    }
}