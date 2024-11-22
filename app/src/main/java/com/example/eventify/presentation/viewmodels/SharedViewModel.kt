package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.eventify.domain.model.PlaceCoordinates

class SharedViewModel : ViewModel() {
    var sharedCoordinates: PlaceCoordinates? = null

    fun setCoordinates(placeCoordinates: PlaceCoordinates) {
        sharedCoordinates = placeCoordinates
    }
}