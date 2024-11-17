package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventify.domain.model.VenueItem
import com.example.eventify.domain.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueViewModel @Inject constructor(
    private val venueRepository: VenueRepository
) : ViewModel() {

    val venues = MutableStateFlow<List<VenueItem>>(emptyList())

    init {
        getVenues()
    }

    private fun getVenues() {
        viewModelScope.launch {
            val response = venueRepository.getVenues()
            venues.emit(response)
        }
    }
}