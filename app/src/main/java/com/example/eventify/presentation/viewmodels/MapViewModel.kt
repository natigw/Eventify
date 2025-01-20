package com.example.eventify.presentation.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.places.SearchItem
import com.example.domain.model.places.event.EventItem
import com.example.domain.model.places.venue.VenueItem
import com.example.domain.repository.EventRepository
import com.example.domain.repository.VenueRepository
import com.example.eventify.NetworkUtils
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MapViewModel @Inject constructor(
    @Named("ThemeChoice")
    var sharedPrefTheme: SharedPreferences,
    private val venueRepository: VenueRepository,
    private val eventRepository: EventRepository
) : ViewModel() {


    val markerList = mutableListOf<Marker>()


    val eventsState = MutableStateFlow<List<EventItem>?>(null)
    val venuesState = MutableStateFlow<List<VenueItem>?>(null)

    val searchState = MutableStateFlow<List<SearchItem>?>(null)
    val inputState = MutableStateFlow<String?>(null)
    val isLoading = MutableStateFlow<Boolean?>(null)

    private val searchItems = mutableListOf<SearchItem>()

    enum class Filter{
        ALL,
        VENUES,
        EVENTS
    }




    init {
        getEvents()
        getVenues()
    }

    private fun getEvents(){
        viewModelScope.launch {
            val checkIfValid = NetworkUtils.handleInvalidAccessToken()
            if(checkIfValid){
                try {
                    val response = eventRepository.getEvents()
                    eventsState.update {response}
                }catch (_:Exception){}
            }
        }
    }

    private fun getVenues(){
        viewModelScope.launch {
            venuesState.update {
                venueRepository.getVenues()
            }
        }
    }

    fun findMarkerByLatLng(lat: Double, lng: Double): Marker? {
        return markerList.find { marker ->
            marker.position.latitude == lat && marker.position.longitude == lng
        }
    }

    fun searchPlaces(query : String, filter : Filter = Filter.ALL){
        viewModelScope.launch {
            searchItems.clear()

            try {
                isLoading.update { true }
                val eventsResponse = eventRepository.searchEvent(query)
                val venuesResponse = venueRepository.searchVenue(query)

                when(filter){
                    Filter.ALL->{
                        searchItems.addAll(eventsResponse)
                        searchItems.addAll(venuesResponse)
                    }
                    Filter.EVENTS->{
                        searchItems.addAll(eventsResponse)
                    }
                    Filter.VENUES->{
                        searchItems.addAll(venuesResponse)
                    }
                }

                Log.e("RealItems",searchItems.size.toString())
                searchState.update { searchItems.toList() }
            }
            catch (_:Exception){

            }
            finally {
                isLoading.update { false }
            }

        }
    }
}