package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.event.EventItem
import com.example.domain.model.places.venue.VenueItem
import com.example.domain.repository.EventRepository
import com.example.domain.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    val venueRepository: VenueRepository,
    val eventRepository: EventRepository
) : ViewModel() {
    val eventsState = MutableStateFlow<List<EventItem>?>(null)
    val venuesState = MutableStateFlow<List<VenueItem>?>(null)


    init {

    }
    private fun getEvents(){
        viewModelScope.launch {
            eventsState.update {
                eventRepository.getEvents()
            }
        }
    }

    private fun getVenues(){
        viewModelScope.launch {
            venuesState.update {
                venueRepository.getVenues()
            }
        }
    }

}