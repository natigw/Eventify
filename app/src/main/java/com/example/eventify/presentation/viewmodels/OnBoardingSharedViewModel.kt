package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class OnBoardingSharedViewModel : ViewModel() {
    val isRegisteredState = MutableStateFlow(false)
    val userEmailState = MutableStateFlow<String?>(null)


    fun setFromRegisterScreen(){
        isRegisteredState.update { true }
    }

    fun setUserEmail(userEmail : String){
        userEmailState.update { userEmail }
    }
}