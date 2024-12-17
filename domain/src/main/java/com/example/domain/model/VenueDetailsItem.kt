package com.example.domain.model

import com.google.android.gms.maps.model.LatLng

data class VenueDetailsItem (
    val venueId: Int,
    val title: String,
    val description: String,
    val imageLinks: List<Any>, //TODO string et
    val venueType: String,
    val openHours: String,
    val likeCount: Int,
    val rating: Double,
    val coordinates: LatLng
)