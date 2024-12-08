package com.example.eventify.data.remote.repository

import android.util.Log
import com.example.eventify.common.utils.randomDouble
import com.example.eventify.common.utils.randomInteger
import com.example.eventify.common.utils.roundDouble
import com.example.eventify.data.remote.api.VenueAPI
import com.example.eventify.domain.model.VenueDetailsItem
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
                        placeId = it.id,
                        name = it.name,
                        imageLink = it.image1Link,
                        description = it.description,
                        venueType = it.venueType,
                        openHour = it.workHoursOpen,
                        closeHour = it.workHoursClose,
                        likeCount = it.numLikes,
                        latCoordinate = if (it.lat != "string") it.lat.toDouble() else 0.0,
                        lngCoordinate = if (it.lng != "string") it.lng.toDouble() else 0.0
                    )
                }
            }

        }

        Log.e("venueRequestError", response.errorBody().toString())
        return emptyList()
    }

    override suspend fun getVenueDetails(venueId: Int): VenueDetailsItem? {
        val response = api.getVenueDetails(venueId)
        if (response.isSuccessful) {
            response.body()?.let { rawData ->
                return VenueDetailsItem(
                    venueId = rawData.id,
                    title = rawData.name,
                    description = rawData.description,
                    imageLinks = listOf(rawData.image1Link, rawData.image2Link, rawData.image3Link),
                    venueType = rawData.venueType,
                    openHours = "${rawData.workHoursOpen.substring(0, 5)} - ${rawData.workHoursClose.substring(0, 5)}",
                    likeCount = rawData.numLikes,
                    rating = roundDouble(randomDouble(max = 5.0))
                )
            }
        }

        Log.e("venueDetailsRequestError", response.errorBody().toString())
        return null
    }

}