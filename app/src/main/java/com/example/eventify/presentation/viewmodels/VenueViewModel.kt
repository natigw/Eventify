package com.example.eventify.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.venue.VenueItem
import com.example.domain.repository.VenueRepository
import com.example.eventify.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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
            val checkIfValid = NetworkUtils.handleInvalidAccessToken()
            if (checkIfValid) {
                try {
                    val response = venueRepository.getVenues()
                    venues.emit(response)
                } catch (e: Exception) {
                    Log.e("network venue", e.toString())
                }
            }
        }
    }

    fun likeVenue(venueId: Int) {
        viewModelScope.launch {
//            venueRepository.
        }
    }
}