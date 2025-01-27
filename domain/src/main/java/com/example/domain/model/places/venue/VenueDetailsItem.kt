package com.example.domain.model.places.venue

import com.google.android.gms.maps.model.LatLng

data class VenueDetailsItem (
    val venueId: Int,
    val title: String,
    val description: String,
    val imageLink: String,
    val venueType: String,
    val openHours: String,
    val likeCount: Int,
    val rating: Double,
    val coordinates: LatLng
)