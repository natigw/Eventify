package com.example.data.remote.repository

import android.util.Log
import com.example.common.utils.functions.dateFormatterYMDtoDMY
import com.example.common.utils.functions.randomDouble
import com.example.data.remote.api.EventAPI
import com.example.data.remote.model.events.comment.addComment.RequestAddEventComment
import com.example.domain.model.AddCommentItem
import com.example.domain.model.CommentItem
import com.example.domain.model.EventDetailsItem
import com.example.domain.model.EventItem
import com.example.domain.repository.EventRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val api: EventAPI
) : EventRepository {

    override suspend fun getEvents(): List<EventItem> {
        val response = api.getAllEvents()
        if(response.isSuccessful){
            response.body()?.let { rawData->
                return rawData.map { event ->
                    EventItem(
                        eventId = event.id,
                        name = event.title,
                        imageLink = event.posterImageLink,
                        eventDateTime = if (event.start.substring(0, 5) == event.finish.substring(0, 5)) "${dateFormatterYMDtoDMY(event.date.substring(0,10))} • all the day" else "${dateFormatterYMDtoDMY(event.date.substring(0,10))} • ${event.start.substring(0, 5)}-${event.finish.substring(0, 5)}"
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
                    eventId = rawData.event.id,
                    title = rawData.event.title,
                    description = rawData.event.description,
                    imageLinks = listOf(rawData.event.posterImageLink),
                    eventType = rawData.event.eventType.replace('_', ' ').replaceFirstChar { it.uppercaseChar() },
                    organizer = if (rawData.event.organizerId == 1) "Eventify Group" else "Organizer${rawData.event.organizerId}", //TODO -> backend
                    eventDate = rawData.event.date.substring(0,10),
                    publishingDate = "${rawData.event.createdAt.substring(0,10)}, ${rawData.event.createdAt.substring(12)}",
                    eventDurationHours = if (rawData.event.start.substring(0, 5) == rawData.event.finish.substring(0, 5)) "all the day" else "${rawData.event.start.substring(0, 5)} - ${rawData.event.finish.substring(0, 5)}",
                    likeCount = rawData.event.numLikes,
                    rating = randomDouble(max = 5.0),
                    coordinates = if (rawData.location.lat != "string") LatLng(rawData.location.lat.toDouble(), rawData.location.lng.toDouble()) else LatLng(0.0, 0.0)
                )
            }
        }
        return null
    }

    override suspend fun getEventComments(eventId: Int): List<CommentItem> {
        val response = api.getEventComments(eventId)
        if (response.isSuccessful) {
            response.body()?.let { rawComment ->
                return rawComment.map {
                    //val details = api.getVenueCommentDetails(it.id)  //heleki eyni datani qaytarir - useless<<
                    CommentItem(
                        commentId = it.id,
                        username = it.ownerId.toString(),
                        content = it.content,
                        date = "${it.createdAt.substring(0,10)}, ${it.createdAt.substring(11, 16)}"
                    )
                }
            }
        }
        Log.e("venueCommentRequestError", response.errorBody().toString())
        return emptyList()
    }

    override suspend fun addEventComment(requestAddEventComment: AddCommentItem) {
        api.addEventComment(
            RequestAddEventComment(
                content = requestAddEventComment.content,
                event = requestAddEventComment.placeId
            )
        )
        //TODO -> successful olub olmadigini check ele
    }
}