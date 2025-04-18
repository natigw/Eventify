package com.example.data.remote.repository

import com.example.common.utils.functions.dateFormatterIFYEAR_MNAMED_Comma_HM
import com.example.common.utils.functions.dateFormatter_RemoveDashes_YMDtoDMY
import com.example.common.utils.functions.randomDouble
import com.example.data.remote.api.EventAPI
import com.example.data.remote.model.events.comment.addComment.RequestAddEventComment
import com.example.data.remote.model.events.likeDislike.RequestLikeDislikeEvent
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.domain.model.places.FavoriteItem
import com.example.domain.model.places.SearchItem
import com.example.domain.model.places.event.CreateCustomEventRequestItem
import com.example.domain.model.places.event.EventDetailsItem
import com.example.domain.model.places.event.EventItem
import com.example.domain.repository.EventRepository
import com.google.android.gms.maps.model.LatLng
import okhttp3.MultipartBody
import java.time.Instant
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val api: EventAPI
) : EventRepository {

    override suspend fun getEvents(): List<EventItem> {
        try {
            val response = api.getAllEvents()
            val responseLiked = api.getFavEventIDs()

            if (response.isSuccessful && response.body() != null) {
                response.body()!!.let { events->
                    responseLiked.body()!!.let { favorites->
                        return events.map{ event->
                            EventItem(
                                eventId = event.id,
                                name = event.title,
                                imageLink = event.posterImageLink,
                                eventDateTime = if (event.start.substring(0, 5) == event.finish.substring(0, 5)) "${dateFormatter_RemoveDashes_YMDtoDMY(event.date.substring(0, 10))} • all the day" else "${dateFormatter_RemoveDashes_YMDtoDMY(event.date.substring(0, 10))} • ${event.start.substring(0, 5)}-${event.finish.substring(0, 5)}",
                                lat = event.lat.toDouble(),
                                lng = event.lng.toDouble(),
                                isLiked = favorites.contains(event.id)
                            )
                        }
                    }
                }

            } else {
                throw Exception(response.errorBody()?.string())
            }

        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getEventDetails(eventId: Int): EventDetailsItem {
        try {
            val response = api.getEventDetails(eventId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.let { rawData ->
                    return EventDetailsItem(
                        eventId = rawData.event.id,
                        title = rawData.event.title,
                        description = rawData.event.description,
                        imageLink = rawData.event.posterImageLink,
                        eventType = rawData.event.eventType.replace('_', ' ').replaceFirstChar { it.uppercaseChar() },
                        organizer = if (rawData.event.organizerId == 1) "not specified" else "OrganizerId:${rawData.event.organizerId}",
                        eventDate = rawData.event.date.substring(0, 10),
                        publishingDate = "${rawData.event.createdAt.substring(0, 10)}, ${rawData.event.createdAt.substring(12)}",
                        eventDurationHours = if (rawData.event.start.substring(0, 5) == rawData.event.finish.substring(0, 5)) "all the day" else "${rawData.event.start.substring(0, 5)} - ${rawData.event.finish.substring(0, 5)}",
                        likeCount = rawData.event.numLikes,
                        rating = randomDouble(max = 5.0),
                        coordinates = if (rawData.location.lat != "string") LatLng(rawData.location.lat.toDouble(), rawData.location.lng.toDouble()) else LatLng(0.0, 0.0)
                    )
                }
            } else {
                throw Exception(response.errorBody()?.string())
            }

        } catch (e: Exception) {
            throw Exception("Network error!")
        }
    }

    override suspend fun createCustomEvent(requestCreateEvent: CreateCustomEventRequestItem) {
        try {
//            if (successful..)
//            api.createCustomEvent(
//                RequestCreateCustomEvent(
//                    title = requestCreateEvent.title,
//                    description = requestCreateEvent.description,
//                    eventType = requestCreateEvent.eventType.toString(),
//                    posterImageLink = requestCreateEvent.posterImageLink,
//                    date = requestCreateEvent.date,
//                    start = requestCreateEvent.start,
//                    finish = requestCreateEvent.finish,
//                    lat = requestCreateEvent.lat,
//                    lng = requestCreateEvent.lng
//                )
//            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getEventComments(eventId: Int): List<CommentItem> {
        try {
            val response = api.getEventComments(eventId)
            if (response.isSuccessful) {
                response.body()!!.let { rawComment ->
                    return rawComment.map {
                        CommentItem(
                            ownerId = it.owner.id,
                            commentId = it.comment.id,
                            username = it.owner.username,
                            content = it.comment.content,
                            date = dateFormatterIFYEAR_MNAMED_Comma_HM(Instant.parse("${it.comment.createdAt}Z")),
                            isPending = false
                        )
                    }
                }
            } else {
                throw Exception(response.errorBody()?.string())
            }

        } catch (e: Exception) {
            throw Exception("Network error!")
        }
    }

    override suspend fun addEventComment(requestAddEventComment: AddCommentItem) : Boolean{
        return try {
            api.addEventComment(
                RequestAddEventComment(
                    content = requestAddEventComment.content,
                    event = requestAddEventComment.placeId
                )
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getFavEvents(): List<FavoriteItem> {
        try {
            val response = api.getFavEvents()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.let { rawFavEvents ->
                    return rawFavEvents.map {
                        FavoriteItem(
                            id = it.id,
                            imageLink = it.posterImageLink,
                            title = it.title,
                            description = it.description,
                            date = dateFormatter_RemoveDashes_YMDtoDMY(it.date.substring(0,10))
                        )
                    }
                }
            } else {
                throw Exception(response.errorBody()?.string())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getFavEventsID(): List<Int> {
        try {
            val response = api.getFavEventIDs()
            if(response.isSuccessful && response.body()!=null){
               return response.body()!!
            }
            else{
                throw Exception(response.errorBody()?.string())
            }
        }
        catch (e : Exception){
            throw e
        }
    }


    override suspend fun likeEvent(eventId: Int) {
        try {
            api.likeEvent(
                RequestLikeDislikeEvent(
                    event = eventId
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun searchEvent(query: String): List<SearchItem> {
        try {
            val response = api.searchEvent(query)
            if(response.isSuccessful && response.body()!=null){
                return response.body()!!.map {
                    SearchItem(
                        placeId = it.id,
                        name = it.title,
                        lat = it.lat,
                        lng = it.lng,
                        placeType = "event"
                    )
                }
            }
            else{
                throw Exception(response.errorBody()?.string())
            }
        }
        catch (e : Exception){
            throw e
        }
    }

    override suspend fun uploadFileAndGetLink(multipartBody: MultipartBody.Part): String {
        try {
            val response = api.uploadFileAndGetLink(destination = "events", multipartBody)
            return response.body()!!
        }
        catch (e : Exception){
            throw e
        }
    }

}