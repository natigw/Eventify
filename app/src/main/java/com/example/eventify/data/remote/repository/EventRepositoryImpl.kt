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
        return response.map {
            val event = it.event
            val location = it.location
            EventItem(
                id = event.id,
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
                latCoordinate = location.lat,
                lngCoordinate = location.lng
            )
        }
    }

}