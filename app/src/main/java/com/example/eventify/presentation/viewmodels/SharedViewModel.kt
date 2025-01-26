package com.example.eventify.presentation.viewmodels

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.example.domain.model.places.PlaceCoordinates
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SharedViewModel : ViewModel() {

    val sharedRouteDestinationCoordinates = MutableSharedFlow<LatLng>(replay = 0)

    var sharedCoordinates: PlaceCoordinates? = null


    var eventsRVState : Parcelable? = null
    var venuesRVState : Parcelable? = null
    suspend fun setCoordinates(latLng: LatLng) {
        sharedRouteDestinationCoordinates.emit(latLng)
    }

    suspend fun setRouteCoordinates(routeCoordinates: LatLng) {
        sharedRouteDestinationCoordinates.emit(routeCoordinates)
    }

}