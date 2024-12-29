package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.event.EventItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReferralViewModel @Inject constructor(
    private val eventsRepositoryImpl: com.example.domain.repository.EventRepository
): ViewModel(){
    val eventsState = MutableStateFlow<List<EventItem>>(emptyList())

    init {
        viewModelScope.launch {
            eventsState.update {
                eventsRepositoryImpl.getEvents()
            }
        }
    }
}