package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventify.domain.model.EventItem
import com.example.eventify.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val EventRepository: EventRepository
) : ViewModel() {

    val Events = MutableStateFlow<List<EventItem>>(emptyList())

    init {
        getEvents()
    }

    private fun getEvents() {
        viewModelScope.launch {
            val response = EventRepository.getEvents()
            Events.emit(response)
        }
    }
}