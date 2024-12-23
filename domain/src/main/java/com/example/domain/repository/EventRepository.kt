package com.example.domain.repository

import com.example.domain.model.AddCommentItem
import com.example.domain.model.CommentItem
import com.example.domain.model.EventDetailsItem
import com.example.domain.model.EventItem

interface EventRepository {
    suspend fun getEvents(): List<EventItem>
    suspend fun getEventDetails(eventId: Int): EventDetailsItem?
    suspend fun getEventComments(eventId: Int): List<CommentItem>

//    suspend fun getEventCommentDetails(commentId: Int): ResponseEventCommentDetails

    suspend fun addEventComment(
        requestAddEventComment: AddCommentItem
    )
}