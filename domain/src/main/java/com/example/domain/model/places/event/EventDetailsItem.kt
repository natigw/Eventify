package com.example.domain.model.places.event

import com.google.android.gms.maps.model.LatLng

data class EventDetailsItem (
    val eventId: Int,
    val title: String,
    val description: String,
    val imageLink: String,
    val eventType: String,
    val organizer: String,
    val publishingDate: String,
    val eventDate: String,
    val eventDurationHours: String,
    val likeCount: Int,
    val rating: Double,
    val coordinates: LatLng
)