package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.venue.VenueItem
import com.example.domain.repository.VenueRepository
import com.example.eventify.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueViewModel @Inject constructor(
    private val venueRepository: VenueRepository
) : ViewModel() {

    val venues = MutableStateFlow<List<VenueItem>?>(null)

    init {
        getVenues()
    }

    private fun getVenues() {
        viewModelScope.launch {
            try {
                val checkIfValid = NetworkUtils.handleInvalidAccessToken()
                if (checkIfValid) {
                    val response = venueRepository.getVenues()
                    venues.emit(response)
                }
            }
            catch (_:Exception){}
        }
    }

//    fun likeVenue(venueId: Int) {
//        viewModelScope.launch {
//            venueRepository.
//        }
//    }
}