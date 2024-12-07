package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventify.domain.model.VenueDetailsItem
import com.example.eventify.domain.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarkerDetailsViewModel @Inject constructor(
    private val venueDetailsRepository: VenueRepository
) : ViewModel() {

    val venueDetails = MutableStateFlow<VenueDetailsItem?>(null)

    fun getDetails(venueId: Int) {
        viewModelScope.launch {
            val response = venueDetailsRepository.getVenueDetails(venueId)
            venueDetails.emit(response)
        }
    }
}