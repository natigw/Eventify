package com.example.eventify.data.remote.repository

import com.example.eventify.data.remote.api.EventifyAPI
import com.example.eventify.domain.model.VenueItem
import com.example.eventify.domain.repository.VenueRepository
import javax.inject.Inject

class VenueRepositoryImpl @Inject constructor(
    private val api: EventifyAPI
) : VenueRepository {

    override suspend fun getVenues(): List<VenueItem> {
        val response = api.getAllVenues()
        return response.map {
            VenueItem(
                id = it.id,
                name = it.name,
                imageLink = it.image1Link,
                description = it.description,
                latCoordinate = it.lat,
                lngCoordinate = it.lng
            )
        }
    }

}