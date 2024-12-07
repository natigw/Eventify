package com.example.eventify.domain.repository

import com.example.eventify.domain.model.VenueDetailsItem
import com.example.eventify.domain.model.VenueItem

interface VenueRepository {
    suspend fun getVenues(): List<VenueItem>
    suspend fun getVenueDetails(venueId: Int) : VenueDetailsItem?
}