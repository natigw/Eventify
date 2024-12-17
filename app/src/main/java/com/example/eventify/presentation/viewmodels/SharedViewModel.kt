package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.domain.model.PlaceCoordinates
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow

class SharedViewModel : ViewModel() {

    val sharedRouteDestinationCoordinates = MutableStateFlow<LatLng?>(null)

    var sharedCoordinates: com.example.domain.model.PlaceCoordinates? = null

    fun setCoordinates(placeCoordinates: com.example.domain.model.PlaceCoordinates) {
        sharedCoordinates = placeCoordinates
    }

    suspend fun setRouteCoordinates(routeCoordinates: LatLng) {
        sharedRouteDestinationCoordinates.emit(routeCoordinates)
    }

}