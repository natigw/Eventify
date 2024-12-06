package com.example.eventify.presentation.viewmodels

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventify.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    @Named("UserTokens") val sharedPreferences: SharedPreferences,
    val authRepository: AuthRepository
): ViewModel() {


    fun checkRefreshTokenIsValid(refreshToken : String) : Boolean{
        return try {
            viewModelScope.launch {
                val response = authRepository.refreshAccessToken(refreshToken)
                sharedPreferences.edit {
                    putString("access_token",response.accessToken)
                }
            }
            true
        }
        catch (e:Exception){

            false
        }
    }

    fun getRefreshToken() : String?{
        return sharedPreferences.getString("refresh_token",null)
    }

    fun clearTokens(){
        sharedPreferences.edit{
            clear()
        }
    }


}