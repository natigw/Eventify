package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AddCommentItem
import com.example.domain.model.CommentItem
import com.example.domain.model.EventDetailsItem
import com.example.domain.repository.EventRepository
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

    fun getComments(eventId: Int) {
        viewModelScope.launch {
            val response = eventRepository.getEventComments(eventId)
            comments.emit(response)
            isLoadingComments.update { false }
            if (response.isEmpty()) noComments = true
        }
    }

    fun addComment(comment: AddCommentItem) {
        viewModelScope.launch {
            eventRepository.addEventComment(comment)
        }
    }
}