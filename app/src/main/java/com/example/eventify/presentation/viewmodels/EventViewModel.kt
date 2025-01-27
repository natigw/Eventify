package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.event.EventItem
import com.example.domain.repository.EventRepository
import com.example.eventify.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    val events = MutableStateFlow<List<EventItem>?>(null)

    init {
        getEvents()
    }

    private fun getEvents() {
        viewModelScope.launch {
            try{
                val checkIfValid = NetworkUtils.handleInvalidAccessToken()
                if(checkIfValid){
                    val response = eventRepository.getEvents()
                    events.emit(response)
                }
            }
            catch (_:Exception){}

        }
    }

    fun likeEvent(eventId: Int) {
        viewModelScope.launch {
            eventRepository.likeEvent(eventId)
        }
    }
}