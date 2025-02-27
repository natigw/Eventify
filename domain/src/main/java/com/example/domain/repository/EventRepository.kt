package com.example.domain.repository

import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.domain.model.places.FavoriteItem
import com.example.domain.model.places.SearchItem
import com.example.domain.model.places.event.CreateCustomEventRequestItem
import com.example.domain.model.places.event.EventDetailsItem
import com.example.domain.model.places.event.EventItem
import okhttp3.MultipartBody

interface EventRepository {
    suspend fun getEvents(): List<EventItem>

    suspend fun getEventDetails(eventId: Int): EventDetailsItem?

    suspend fun createCustomEvent(requestCreateEvent: CreateCustomEventRequestItem) //:


    suspend fun getEventComments(eventId: Int): List<CommentItem>

//    suspend fun getEventCommentDetails(commentId: Int): ResponseEventCommentDetails

    suspend fun addEventComment(
        requestAddEventComment: AddCommentItem
    ) : Boolean

    suspend fun getFavEvents(): List<FavoriteItem>

    suspend fun getFavEventsID(): List<Int>

    suspend fun likeEvent(eventId: Int)

    suspend fun searchEvent(query: String) : List<SearchItem>

    suspend fun uploadFileAndGetLink(multipartBody: MultipartBody.Part): String

}