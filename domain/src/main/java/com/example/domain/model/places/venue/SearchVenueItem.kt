package com.example.domain.model.places.venue

data class SearchVenueItem (
    val venueId : Int,
    val title : String,
    val imageLink : String?,
    val description : String,
    val lat : String,
    val lng : String
)