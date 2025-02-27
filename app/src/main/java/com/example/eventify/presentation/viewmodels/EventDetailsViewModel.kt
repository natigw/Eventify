package com.example.eventify.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.remote.api.ITicketAPI
import com.example.domain.model.auth.UserData
import com.example.domain.model.iTicket.SearchITicketItem
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.domain.model.places.event.EventDetailsItem
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.EventRepository
import com.example.eventify.NetworkUtils
import com.example.eventify.presentation.viewmodels.EventDetailsViewModel.ITicketRetrofitInstance.api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class EventDetailsViewModel @Inject constructor(
    private val eventRepository: EventRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private object ITicketRetrofitInstance {
        val api: ITicketAPI by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.iticket.az/en/v5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ITicketAPI::class.java)
        }
    }


    val eventDetails = MutableStateFlow<EventDetailsItem?>(null)


    val isCommentAdded = MutableStateFlow(false)
    val comments = MutableStateFlow<List<CommentItem>?>(null)
    var userInfo : UserData? = null

    var likedState = MutableStateFlow<Boolean?>(null)
    private val lastLikedState = mutableListOf<Boolean>()


    init {
        getUserData()
    }

    fun getEventDetails(eventId: Int) {
        viewModelScope.launch {
            try {
                val response = eventRepository.getEventDetails(eventId)
                eventDetails.emit(response)
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
                    Log.e("network - like",e.message.toString())
                }
            }
        }
    }

    fun getComments(eventId: Int) {
        viewModelScope.launch {
            try {
                val response = eventRepository.getEventComments(eventId)
                comments.emit(response)
            }catch (_:Exception){}
        }
    }

    fun updateLikeEvent(eventId : Int){
        viewModelScope.launch {
            eventRepository.likeEvent(eventId)
        }
    }

    fun addComment(comment: AddCommentItem) {
        viewModelScope.launch {
            try {
                val checkIfValid = NetworkUtils.handleInvalidAccessToken()
                if(checkIfValid){
                    val condition = eventRepository.addEventComment(comment)
                    isCommentAdded.update { condition }
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


    suspend fun getSearchDetails(keyword: String): SearchITicketItem {
        val searchResponse = api.searchITicket(client = "web", query = keyword).response.events[0]
        return SearchITicketItem (category = searchResponse.categorySlug, slug = searchResponse.slug)
    }
}