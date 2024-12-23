package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.AddCommentItem
import com.example.domain.model.CommentItem
import com.example.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventCommentsViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    var noComments = false
    val isLoading = MutableStateFlow(true)
    private val eventTitle = MutableStateFlow<String?>(null)
    val comments = MutableStateFlow<List<CommentItem>>(emptyList())

    fun getComments(eventId: Int) {
        viewModelScope.launch {
            val response = eventRepository.getEventComments(eventId)
            comments.emit(response)
            isLoading.update { false }
            if (response.isEmpty()) noComments = true
        }
    }

    fun getEventName(eventId: Int) {
        viewModelScope.launch {
            val response = eventRepository.getEventDetails(eventId)?.title
            eventTitle.emit(response)
        }
    }

    fun addComment(comment: AddCommentItem) {
        viewModelScope.launch {
            eventRepository.addEventComment(comment)
        }
    }
}