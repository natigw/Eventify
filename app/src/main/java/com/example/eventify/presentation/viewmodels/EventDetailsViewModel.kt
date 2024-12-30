package com.example.eventify.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.domain.model.places.event.EventDetailsItem
import com.example.domain.repository.EventRepository
import com.example.eventify.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    val isLoadingMain = MutableStateFlow(true)
    val eventDetails = MutableStateFlow<EventDetailsItem?>(null)

    var likedState = MutableStateFlow(false)

    var noComments = false
    val isLoadingComments = MutableStateFlow(true)
    val comments = MutableStateFlow<List<CommentItem>>(emptyList())



    fun getEventDetails(eventId: Int) {
        viewModelScope.launch {
            val response = eventRepository.getEventDetails(eventId)
            eventDetails.emit(response)
            isLoadingMain.update { false }
        }
    }



    fun getEventLikeInfo(eventId: Int) {
        viewModelScope.launch {
            val checkIfValid = NetworkUtils.handleInvalidAccessToken()
            if(checkIfValid){
                try {
                    val response = eventRepository.getFavEvents()
                    val like = response.any {
                        it.eventId == eventId
                    }
                    likedState.update { like }
                }
                catch (e : Exception){
                    Log.e("eventLike",e.message.toString())
                }
            }
            }
    }

    fun getComments(eventId: Int) {
        viewModelScope.launch {
            val response = eventRepository.getEventComments(eventId)
            comments.emit(response)
            isLoadingComments.update { false }
            if (response.isEmpty()) noComments = true
        }
    }

    fun updateLikeEvent(eventId : Int){
        viewModelScope.launch {
            eventRepository.likeEvent(eventId)
        }
    }

    fun addComment(comment: AddCommentItem) {
        viewModelScope.launch {
            eventRepository.addEventComment(comment)
        }
    }
}