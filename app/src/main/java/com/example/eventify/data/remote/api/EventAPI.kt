package com.example.eventify.data.remote.api

import com.example.eventify.data.remote.model.events.ResponseAllEvents
import retrofit2.Response
import retrofit2.http.GET

interface EventAPI {
    @GET("/events")
    suspend fun getAllEvents() : Response<List<ResponseAllEvents>>

}