package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventify.domain.model.CommentItem
import com.example.eventify.domain.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueCommentsViewModel @Inject constructor(
    private val venueRepository: VenueRepository
) : ViewModel() {

    val isLoading = MutableStateFlow(true)
    val venueTitle = MutableStateFlow<String?>(null)
    val comments = MutableStateFlow<List<CommentItem>>(emptyList())

    fun getComments(venueId: Int) {
        viewModelScope.launch {
            val response = venueRepository.getVenueComments(venueId)
            comments.emit(response)
            isLoading.update { false }
        }
    }

    fun getVenueName(venueId: Int) {
        viewModelScope.launch {
            val response = venueRepository.getVenueDetails(venueId)?.title
            venueTitle.emit(response)
        }
    }
}