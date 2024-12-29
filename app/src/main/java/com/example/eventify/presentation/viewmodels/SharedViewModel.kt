package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.domain.model.places.PlaceCoordinates
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SharedViewModel : ViewModel() {

    val sharedRouteDestinationCoordinates = MutableStateFlow<LatLng?>(null)

    var sharedCoordinates: PlaceCoordinates? = null

    val testStateFlow = MutableStateFlow<PlaceCoordinates?>(null)

    fun setCoordinates(placeCoordinates: PlaceCoordinates) {
        testStateFlow.update { placeCoordinates }
    }

    suspend fun setRouteCoordinates(routeCoordinates: LatLng) {
        sharedRouteDestinationCoordinates.emit(routeCoordinates)
    }

}