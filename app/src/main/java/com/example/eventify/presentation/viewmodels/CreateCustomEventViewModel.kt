package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateCustomEventViewModel @Inject constructor(
    private val eventRepository: EventRepository
): ViewModel() {

    val pickedDate = MutableStateFlow<LocalDate?>(null)
    val pickedStartTime = MutableStateFlow<LocalTime?>(null)
    val pickedFinishTime = MutableStateFlow<LocalTime?>(null)

    fun createCustomEvent() {

    }
}