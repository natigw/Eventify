package com.example.eventify.data.remote.repository

import android.util.Log
import com.example.eventify.data.remote.api.VenueAPI
import com.example.eventify.domain.model.VenueItem
import com.example.eventify.domain.repository.VenueRepository
import javax.inject.Inject

class VenueRepositoryImpl @Inject constructor(
    private val api: VenueAPI
) : VenueRepository {

    override suspend fun getVenues(): List<VenueItem> {
        val response = api.getAllVenues()
        if(response.isSuccessful){
            response.body()?.let { rawData ->
                return rawData.map {
                    VenueItem(
                        id = it.id,
                        name = it.name,
                        imageLink = it.image1Link,
                        description = it.description,
                        venueType = it.venueType,
                        openHour = it.workHoursOpen,
                        closeHour = it.workHoursClose,
                        likeCount = it.numLikes,
                        latCoordinate = it.lat,
                        lngCoordinate = it.lng
                    )
                }
            }

        }

        Log.e("venueRequestError", response.errorBody().toString())
        return emptyList()
    }

}