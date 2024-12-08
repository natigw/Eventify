package com.example.eventify.data.remote.repository

import com.example.eventify.data.remote.api.EventAPI
import com.example.eventify.domain.model.EventItem
import com.example.eventify.domain.repository.EventRepository
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val api: EventAPI
) : EventRepository {

    override suspend fun getEvents(): List<EventItem> {
        val response = api.getAllEvents()
        if(response.isSuccessful){
            response.body()?.let { data->
                return data.map {
                    val event = it.event
                    val location = it.location
                    EventItem(
                        placeId = event.id,
                        name = event.title,
                        imageLink = event.posterImageLink,
                        description = event.description,
                        eventType = event.eventType,
                        organizerId = event.organizerId,
                        eventDate = event.date,
                        publishingDate = event.createdAt,
                        startHour = event.start,
                        endHour = event.finish,
                        likeCount = event.numLikes,
                        latCoordinate = if (location.lat != "string") location.lat.toDouble() else 0.0,
                        lngCoordinate = if (location.lng != "string") location.lng.toDouble() else 0.0
                    )
                }
            }
        }
        return emptyList()

    }

}