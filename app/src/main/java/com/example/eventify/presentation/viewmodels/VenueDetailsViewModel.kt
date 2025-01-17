package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.domain.model.places.venue.VenueDetailsItem
import com.example.domain.repository.VenueRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
    val commentsState = MutableStateFlow<List<CommentItem>?>(value = null)

    fun getVenueDetails(venueId: Int) {
        viewModelScope.launch {
            try {
                val response = venueRepository.getVenueDetails(venueId)
                venueDetails.emit(response)
                isLoadingMain.update { false }
            }
            catch (_:Exception){}
        }
    }

    fun getComments(venueId: Int) {
        viewModelScope.launch {
            try {
                val response = venueRepository.getVenueComments(venueId)
                commentsState.emit(response)
                isLoadingComments.update { false }
                if (response.isEmpty()) noComments = true
            }
            catch (_:Exception){}
        }
    }

    suspend fun addComment(comment: AddCommentItem) : Boolean{
        return viewModelScope.async {
            venueRepository.addVenueComment(comment)
        }.await()
    }
}