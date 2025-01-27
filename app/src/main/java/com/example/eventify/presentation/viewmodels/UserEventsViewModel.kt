package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.event.EventItem
import com.example.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserEventsViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    val userEvents = MutableStateFlow<List<EventItem>?>(null)
    val customEvents = MutableStateFlow<List<EventItem>?>(null)

    init {
        getUserEvents()
    }

    private fun getUserEvents() {
        viewModelScope.launch {
            try {
                delay(1500)
                val response = emptyList<EventItem>()//eventRepository.getUserEvents()
                userEvents.emit(response)
            } catch (_: Exception) { }

        }
    }

    fun likeUserEvent(userEventId: Int) {
        viewModelScope.launch {
            eventRepository.likeEvent(userEventId)
        }
    }
}