package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.EventItem
import com.example.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: com.example.domain.repository.EventRepository
) : ViewModel() {

    val isLoading = MutableStateFlow(true)
    val events = MutableStateFlow<List<com.example.domain.model.EventItem>>(emptyList())

    init {
        getEvents()
    }

    private fun getEvents() {
        viewModelScope.launch {
            val response = eventRepository.getEvents()
            events.emit(response)
            isLoading.update { false }
        }
    }
}