package com.example.eventify.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
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

    private fun createCustomEvent() {
        viewModelScope.launch {
            try {

            } catch (e: Exception) {
                Log.e("network event", e.toString())
            }
        }
    }

    fun getLinkImageUpload(multipartBodyPart: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                eventRepository.uploadFileAndGetLink(multipartBodyPart)
            } catch (e: Exception) {
                Log.e("network event", e.toString())
            }
        }
    }
}