package com.example.domain.repository

import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.domain.model.places.event.CreateCustomEventRequestItem
import com.example.domain.model.places.event.EventDetailsItem
import com.example.domain.model.places.event.EventItem
import com.example.domain.model.places.event.FavEventItem

interface EventRepository {

    suspend fun getEvents(): List<EventItem>

    suspend fun getEventDetails(eventId: Int): EventDetailsItem?

    suspend fun createCustomEvent(requestCreateEvent: CreateCustomEventRequestItem) //:


    suspend fun getEventComments(eventId: Int): List<CommentItem>

//    suspend fun getEventCommentDetails(commentId: Int): ResponseEventCommentDetails

    suspend fun addEventComment(
        requestAddEventComment: AddCommentItem
    )

    suspend fun getFavEvents(): List<FavEventItem>

    suspend fun getFavEventsID() : List<Int>

    suspend fun likeEvent(eventId: Int)

    suspend fun dislikeEvent(eventId: Int)
}