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
                        organizer = if (event.organizerId == 1) "Eventify Group" else "Organizer${event.organizerId}", //TODO -> backend
                        eventDate = event.date.substring(0,10),
                        publishingDate = "${event.createdAt.substring(0,10)}, ${event.createdAt.substring(12)}",
                        eventDurationHours = "${event.start.substring(0, 5)} - ${event.finish.substring(0, 5)}",
                        likeCount = event.numLikes,
                        lat = if (location.lat != "string") location.lat.toDouble() else 0.0,
                        lng = if (location.lng != "string") location.lng.toDouble() else 0.0
                    )
                }
            }
        }
        return emptyList()

    }

}