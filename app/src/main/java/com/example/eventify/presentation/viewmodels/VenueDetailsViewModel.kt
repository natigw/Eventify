package com.example.eventify.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.auth.UserData
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.domain.model.places.venue.VenueDetailsItem
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.VenueRepository
import com.example.eventify.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VenueDetailsViewModel @Inject constructor(
    private val venueRepository: VenueRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val venueDetails = MutableStateFlow<VenueDetailsItem?>(null)

    val isCommentAdded = MutableStateFlow(false)

    var userInfo : UserData? = null

    val isLoadingComments = MutableStateFlow(true)
    val commentsState = MutableStateFlow<List<CommentItem>?>(null)


    init {
        getUserData()
    }

    fun getVenueDetails(venueId: Int) {
        viewModelScope.launch {
            try {
                val response = venueRepository.getVenueDetails(venueId)
                venueDetails.emit(response)
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
            }
            catch (_:Exception){}
        }
    }

    fun addComment(comment: AddCommentItem) {
        viewModelScope.launch {
            try {
                val checkIfValid = NetworkUtils.handleInvalidAccessToken()
                if(checkIfValid){
                    val condition = venueRepository.addVenueComment(comment)
                    isCommentAdded.update {
                        condition
                    }
                }
            }
            catch (_:Exception){}
        }
    }

    private fun getUserData(){
        viewModelScope.launch {
            try {
                val response = authRepository.getUserData()
                userInfo = response
            }
            catch (_:Exception){}
        }
    }

}