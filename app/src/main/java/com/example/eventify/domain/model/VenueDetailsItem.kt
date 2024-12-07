package com.example.eventify.domain.model

data class VenueDetailsItem (
    val venueId: Int,
    val title: String,
    val description: String,
    val imageLinks: List<Any>, //TODO string et
    val venueType: String,
    val openHours: String,
    val likeCount: Int,
    val rating: Double
)