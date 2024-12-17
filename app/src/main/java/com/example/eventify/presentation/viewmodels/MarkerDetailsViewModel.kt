package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.VenueDetailsItem
import com.example.domain.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarkerDetailsViewModel @Inject constructor(
    private val venueDetailsRepository: com.example.domain.repository.VenueRepository
) : ViewModel() {

    val venueDetails = MutableStateFlow<com.example.domain.model.VenueDetailsItem?>(null)

    fun getDetails(venueId: Int) {
        viewModelScope.launch {
            val response = venueDetailsRepository.getVenueDetails(venueId)
            venueDetails.emit(response)
        }
    }
}