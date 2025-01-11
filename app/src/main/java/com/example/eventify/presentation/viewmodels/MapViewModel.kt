package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.event.EventItem
import com.example.domain.model.places.venue.VenueItem
import com.example.domain.repository.EventRepository
import com.example.domain.repository.VenueRepository
import com.example.eventify.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MapViewModel @Inject constructor(
    private val venueRepository: VenueRepository,
    private val eventRepository: EventRepository
) : ViewModel() {
    val eventsState = MutableStateFlow<List<EventItem>?>(null)
    val venuesState = MutableStateFlow<List<VenueItem>?>(null)


    init {

        getEvents()
        getVenues()
    }
    private fun getEvents(){
        viewModelScope.launch {
            val checkIfValid = NetworkUtils.handleInvalidAccessToken()
            if(checkIfValid){
                try {
                    val response = eventRepository.getEvents()
                    eventsState.update {response}
                }catch (_:Exception){}
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