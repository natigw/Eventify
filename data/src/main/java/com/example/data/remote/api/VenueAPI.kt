package com.example.data.remote.api

import com.example.data.remote.model.venues.allVenues.ResponseAllVenues
import com.example.data.remote.model.venues.comment.addComment.RequestAddVenueComment
import com.example.data.remote.model.venues.comment.commentDetails.ResponseVenueCommentDetails
import com.example.data.remote.model.venues.comment.deleteComment.RequestDeleteVenueComment
import com.example.data.remote.model.venues.comment.venueComments.ResponseVenueComments
import com.example.data.remote.model.venues.createVenue.RequestCreateVenue
import com.example.data.remote.model.venues.likeDislike.RequestLikeDislikeVenue
import com.example.data.remote.model.venues.search.ResponseVenueSearch
import com.example.data.remote.model.venues.venueDetails.ResponseVenueDetails
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface VenueAPI {
    @GET("/venues")
    suspend fun getAllVenues(): Response<List<ResponseAllVenues>>

    @GET("/venues/{venue_id}")
    suspend fun getVenueDetails(
        @Path("venue_id")
        venueId: Int
    ): Response<ResponseVenueDetails>

    @POST("/venues")
    suspend fun createVenue(
        @Body requestCreateVenue: RequestCreateVenue
    )

    @POST("/venues/like")
    suspend fun likeVenue(
        @Body
        requestLikeVenue: RequestLikeDislikeVenue
    )

    @GET("/venues/{venue_id}/comment")
    suspend fun getVenueComments(
        @Path("venue_id")
        venueId: Int
    ): Response<List<ResponseVenueComments>>

    @GET("/venues/comment/{comment_id}")
    suspend fun getVenueCommentDetails(
        @Path("comment_id")
        commentId: Int
    ): ResponseVenueCommentDetails

    @POST("/venues/comment")
    suspend fun addVenueComment(
        @Body
        requestAddVenueComment: RequestAddVenueComment
    )

    @DELETE("/venues/comment")
    suspend fun deleteVenueComment(
        @Body
        requestDeleteVenueComment: RequestDeleteVenueComment
    )


    @GET("/venues/search/{query}")
    suspend fun searchVenue(
        @Path("query")
        query: String
    ) : Response<List<ResponseVenueSearch>>
}