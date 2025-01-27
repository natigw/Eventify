package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.remote.api.ITicketAPI
import com.example.domain.model.iTicket.SearchITicketItem
import com.example.domain.model.places.event.EventDetailsItem
import com.example.domain.model.places.venue.VenueDetailsItem
import com.example.domain.repository.EventRepository
import com.example.domain.repository.VenueRepository
import com.example.eventify.presentation.viewmodels.MarkerDetailsViewModel.ITicketRetrofitInstance.api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class MarkerDetailsViewModel @Inject constructor(
    private val venueRepository: VenueRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    object ITicketRetrofitInstance {
        val api: ITicketAPI by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.iticket.az/en/v5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ITicketAPI::class.java)
        }
    }

    val venueDetails = MutableStateFlow<VenueDetailsItem?>(null)
    val eventDetails = MutableStateFlow<EventDetailsItem?>(null)

    fun getVenueDetails(venueId: Int) {
        viewModelScope.launch {
            try {
                val response = venueRepository.getVenueDetails(venueId)
                venueDetails.update { response }
            }
            catch (_:Exception){}
        }
    }
    fun getEventDetails(eventId: Int){
        try {
            viewModelScope.launch {
                val response = eventRepository.getEventDetails(eventId)
                eventDetails.update { response }
            }
        }
        catch (_:Exception){}
    }


    suspend fun getSearchDetails(keyword: String): SearchITicketItem {
        val searchResponse = api.searchITicket(client = "web", query = keyword).response.events[0]
        return SearchITicketItem (category = searchResponse.categorySlug, slug = searchResponse.slug)
    }
}