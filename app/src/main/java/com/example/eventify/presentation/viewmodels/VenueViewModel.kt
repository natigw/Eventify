package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventify.domain.model.VenueItem
import com.example.eventify.domain.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueViewModel @Inject constructor(
    private val venueRepository: VenueRepository
) : ViewModel() {

    val isLoading = MutableStateFlow(true)
    val venues = MutableStateFlow<List<VenueItem>>(emptyList())

    init {
        getVenues()
    }

    private fun getVenues() {
        viewModelScope.launch {

            val response = venueRepository.getVenues()
            venues.emit(response)
            isLoading.update { false }

        }
    }
}