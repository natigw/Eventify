package com.example.domain.repository

import com.example.domain.model.CommentItem
import com.example.domain.model.VenueDetailsItem
import com.example.domain.model.VenueItem

interface VenueRepository {

    suspend fun getVenues(): List<VenueItem>

    suspend fun getVenueDetails(venueId: Int): VenueDetailsItem?

    suspend fun getVenueComments(venueId: Int): List<CommentItem>

//    suspend fun getVenueCommentDetails(commentId: Int): ResponseVenueCommentDetails

//    suspend fun addVenueComment(
//        token: String,
//        requestAddVenueComment: RequestAddVenueComment
//    )
//
//    suspend fun deleteVenueComment(
//        token: String,
//        requestDeleteVenueComment: RequestDeleteVenueComment
//    )

}