package com.example.eventify.presentation.viewmodels

import android.graphics.DiscretePathEffect
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.domain.model.places.event.EventDetailsItem
import com.example.domain.repository.EventRepository
import com.example.eventify.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    val isLoadingMain = MutableStateFlow(true)
    val eventDetails = MutableStateFlow<EventDetailsItem?>(null)

    var likedState = MutableStateFlow<Boolean?>(null)

    var noComments = false
    val isLoadingComments = MutableStateFlow(true)
    val comments = MutableStateFlow<List<CommentItem>?>(value = null)
    val lastLikedState = mutableListOf<Boolean>()


    fun getEventDetails(eventId: Int) {
        viewModelScope.launch {
            try {
                val response = eventRepository.getEventDetails(eventId)
                eventDetails.emit(response)
                isLoadingMain.update { false }

            } catch (_:Exception){}
        }
    }



    fun getEventLikeInfo(eventId: Int) {
        viewModelScope.launch {
            val checkIfValid = NetworkUtils.handleInvalidAccessToken()
            Log.e("validity",checkIfValid.toString())
            if(checkIfValid){
                try {
                    val response = eventRepository.getFavEventsID()
                    val like = response.any {
                        it == eventId
                    }
                    lastLikedState.add(like)
                    likedState.update { like }
                }
                catch (e : Exception){
                    Log.e("bilmire",e.message.toString())
                }
            }
        }
    }

    fun getComments(eventId: Int) {
        viewModelScope.launch {
            try {
                val response = eventRepository.getEventComments(eventId)
                comments.emit(response)
                isLoadingComments.update { false }
                if (response.isEmpty()) noComments = true

            }catch (_:Exception){}
        }
    }

    suspend fun updateLikeEvent(eventId : Int){
        withContext(Dispatchers.IO) {
            eventRepository.likeEvent(eventId)
        }
    }

    suspend fun updateDislikeEvent(eventId: Int){
        withContext(Dispatchers.IO) {
            eventRepository.dislikeEvent(eventId)
        }
    }


    fun addComment(comment: AddCommentItem) {
        viewModelScope.launch {
            eventRepository.addEventComment(comment)
        }
    }
}