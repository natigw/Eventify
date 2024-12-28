package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.VenueDetailsItem
import com.example.domain.model.VenueItem
import com.example.domain.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueDetailsViewModel @Inject constructor(
    private val venueRepository: VenueRepository
) : ViewModel() {

    val isLoading = MutableStateFlow(true)
    val venueDetails = MutableStateFlow<VenueDetailsItem?>(null)

    fun getVenueDetails(venueId: Int) {
        viewModelScope.launch {
            val response = venueRepository.getVenueDetails(venueId)
            venueDetails.emit(response)
            isLoading.update { false }
        }
    }
}