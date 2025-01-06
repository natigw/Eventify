package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime

class CreateCustomEventViewModel: ViewModel() {
    var pickedDate: LocalDate? = null
    var pickedStartTime: LocalTime? = null
    var pickedFinishTime: LocalTime? = null
}