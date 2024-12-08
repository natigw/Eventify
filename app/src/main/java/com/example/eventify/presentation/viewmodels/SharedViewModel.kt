package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.eventify.domain.model.PlaceCoordinates
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow

class SharedViewModel : ViewModel() {

    val sharedRouteDestinationCoordinates = MutableStateFlow<LatLng?>(null)

    var sharedCoordinates: PlaceCoordinates? = null

    fun setCoordinates(placeCoordinates: PlaceCoordinates) {
        sharedCoordinates = placeCoordinates
    }

    suspend fun setRouteCoordinates(routeCoordinates: LatLng) {
        sharedRouteDestinationCoordinates.emit(routeCoordinates)
    }

}