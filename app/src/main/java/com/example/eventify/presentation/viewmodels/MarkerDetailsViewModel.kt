package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.event.EventDetailsItem
import com.example.domain.model.places.venue.VenueDetailsItem
import com.example.domain.repository.EventRepository
import com.example.domain.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarkerDetailsViewModel @Inject constructor(
    private val venueRepository: VenueRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    val venueDetails = MutableStateFlow<VenueDetailsItem?>(null)
    val eventDetails = MutableStateFlow<EventDetailsItem?>(null)

    fun getVenueDetails(venueId: Int) {
        viewModelScope.launch {
            try {
                val response = venueRepository.getVenueDetails(venueId)
                venueDetails.update { response }
            }
            catch (_:Exception){}
        }
    }
    fun getEventDetails(eventId: Int){
        try {
            viewModelScope.launch {
                val response = eventRepository.getEventDetails(eventId)
                eventDetails.update { response }
            }
        }
        catch (_:Exception){}
    }



}