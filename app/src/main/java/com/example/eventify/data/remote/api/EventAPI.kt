package com.example.eventify.data.remote.api

import com.example.eventify.data.remote.model.events.ResponseAllEvents
import com.example.eventify.data.remote.model.events.eventDetails.ResponseEventDetails
import com.example.eventify.data.remote.model.venues.venueDetails.ResponseVenueDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EventAPI {
    @GET("/events")
    suspend fun getAllEvents() : Response<List<ResponseAllEvents>>

    @GET("/events/{event_id}")
    suspend fun getEventDetails(
        @Path("event_id") eventId: Int
    ) : Response<ResponseEventDetails>
}