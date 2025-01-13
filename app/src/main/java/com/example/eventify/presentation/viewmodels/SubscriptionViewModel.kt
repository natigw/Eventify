package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SubscriptionViewModel : ViewModel() {

    var switchState = MutableStateFlow<Boolean>(false)

    fun changeSwitchState() {
        switchState.update { !switchState.value }
    }
}