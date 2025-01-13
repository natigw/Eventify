package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class SubscriptionViewModel : ViewModel() {

    var switchStateFlow = MutableStateFlow(false)

    fun changeState() {
        switchStateFlow.value = !(switchStateFlow.value)
    }
}