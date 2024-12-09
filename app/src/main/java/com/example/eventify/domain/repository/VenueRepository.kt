package com.example.eventify.domain.repository

import com.example.eventify.data.remote.model.venues.comment.addComment.RequestAddVenueComment
import com.example.eventify.data.remote.model.venues.comment.commentDetails.ResponseVenueCommentDetails
import com.example.eventify.data.remote.model.venues.comment.deleteComment.RequestDeleteVenueComment
import com.example.eventify.data.remote.model.venues.comment.venueComments.ResponseVenueComments
import com.example.eventify.domain.model.CommentItem
import com.example.eventify.domain.model.VenueDetailsItem
import com.example.eventify.domain.model.VenueItem

interface VenueRepository {

    suspend fun getVenues(): List<VenueItem>

    suspend fun getVenueDetails(venueId: Int): VenueDetailsItem?

    suspend fun getVenueComments(venueId: Int): List<CommentItem>

    suspend fun getVenueCommentDetails(commentId: Int): ResponseVenueCommentDetails

    suspend fun addVenueComment(
        token: String,
        requestAddVenueComment: RequestAddVenueComment
    )

    suspend fun deleteVenueComment(
        token: String,
        requestDeleteVenueComment: RequestDeleteVenueComment
    )

}