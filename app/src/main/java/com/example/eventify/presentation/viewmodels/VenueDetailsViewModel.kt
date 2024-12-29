package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.domain.model.places.venue.VenueDetailsItem
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

    val isLoadingMain = MutableStateFlow(true)
    val venueDetails = MutableStateFlow<VenueDetailsItem?>(null)

    var noComments = false
    val isLoadingComments = MutableStateFlow(true)
    val comments = MutableStateFlow<List<CommentItem>>(emptyList())

    fun getVenueDetails(venueId: Int) {
        viewModelScope.launch {
            val response = venueRepository.getVenueDetails(venueId)
            venueDetails.emit(response)
            isLoadingMain.update { false }
        }
    }

    fun getComments(venueId: Int) {
        viewModelScope.launch {
            val response = venueRepository.getVenueComments(venueId)
            comments.emit(response)
            isLoadingComments.update { false }
            if (response.isEmpty()) noComments = true
        }
    }

    fun addComment(comment: AddCommentItem) {
        viewModelScope.launch {
            venueRepository.addVenueComment(comment)
        }
    }
}