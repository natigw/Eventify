package com.example.domain.repository

import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.domain.model.places.SearchItem
import com.example.domain.model.places.venue.VenueDetailsItem
import com.example.domain.model.places.venue.VenueItem

interface VenueRepository {

    suspend fun getVenues(): List<VenueItem>

    suspend fun getVenueDetails(venueId: Int): VenueDetailsItem

    suspend fun getVenueComments(venueId: Int): List<CommentItem>

//    suspend fun getVenueCommentDetails(commentId: Int): ResponseVenueCommentDetails

    suspend fun addVenueComment(
        requestAddVenueComment: AddCommentItem
    ) : Boolean

//    suspend fun deleteVenueComment(
//        token: String,
//        requestDeleteVenueComment: RequestDeleteVenueComment
//    )

    suspend fun likeVenue(venueId: Int)

    suspend fun searchVenue(query: String): List<SearchItem>

}