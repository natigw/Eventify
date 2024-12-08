package com.example.eventify.domain.model

import com.google.android.gms.maps.model.LatLng

data class EventDetailsItem (
    val venueId: Int,
    val title: String,
    val description: String,
    val imageLinks: List<Any>, //TODO string et
    val eventType: String,
    val eventDuration: String,
    val likeCount: Int,
    val rating: Double,
    val coordinates: LatLng
)