package com.example.eventify.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.event.EventItem
import com.example.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    val isLoading = MutableStateFlow(true)
    val events = MutableStateFlow<List<EventItem>>(emptyList())

    init {
        getEvents()
    }

    private fun getEvents() {
        viewModelScope.launch {
            try {
                val response = eventRepository.getEvents()
                events.emit(response)
                isLoading.update { false }
            } catch(e: Exception) {
                Log.e("network event", e.toString())
            }
        }
    }

    private fun createCustomEvent() {
        viewModelScope.launch {
            try {

            }
            catch (e: Exception) {
                Log.e("network event", e.toString())
            }
        }
    }
}