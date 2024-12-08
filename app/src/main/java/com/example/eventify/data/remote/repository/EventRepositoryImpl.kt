package com.example.eventify.data.remote.repository

import com.example.eventify.common.utils.randomDouble
import com.example.eventify.data.remote.api.EventAPI
import com.example.eventify.domain.model.EventDetailsItem
import com.example.eventify.domain.model.EventItem
import com.example.eventify.domain.repository.EventRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val api: EventAPI
) : EventRepository {

    override suspend fun getEvents(): List<EventItem> {
        val response = api.getAllEvents()
        if(response.isSuccessful){
            response.body()?.let { rawData->
                return rawData.map {
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

    override suspend fun getEventDetails(eventId: Int): EventDetailsItem? {
        val response = api.getEventDetails(eventId)
        if (response.isSuccessful) {
            response.body()?.let { rawData ->
                return EventDetailsItem(
                    venueId = rawData.event.id,
                    title = rawData.event.title,
                    description = rawData.event.description,
                    imageLinks = listOf(rawData.event.posterImageLink),
                    eventType = rawData.event.eventType,
                    eventDuration = "${rawData.event.start.substring(0, 5)} - ${rawData.event.finish.substring(0, 5)}",
                    likeCount = rawData.event.numLikes,
                    rating = randomDouble(max = 5.0),
                    coordinates = if (rawData.location.lat != "string") LatLng(rawData.location.lat.toDouble(), rawData.location.lng.toDouble()) else LatLng(0.0, 0.0)
                )
            }
        }
        return null
    }

}