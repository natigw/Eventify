package com.example.eventify.data.remote.api

import com.example.eventify.data.remote.model.venues.ResponseAllVenues
import com.example.eventify.data.remote.model.venues.comment.ResponseVenueComments
import com.example.eventify.data.remote.model.venues.comment.addComment.RequestAddCommentVenue
import com.example.eventify.data.remote.model.venues.comment.addComment.ResponseAddCommentVenue
import com.example.eventify.data.remote.model.venues.createVenue.RequestCreateVenue
import com.example.eventify.data.remote.model.venues.likeDislike.RequestLikeDislikeVenue
import com.example.eventify.data.remote.model.venues.venueDetails.ResponseVenueDetails
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface VenueAPI {
    @GET("/venues")
    suspend fun getAllVenues() : Response<List<ResponseAllVenues>>

    @GET("/venues/{venue_id}")
    suspend fun getVenueDetails(
        @Path("venue_id")
        venueId: Int
    ) : Response<ResponseVenueDetails>

//    @POST("/venues")
//    suspend fun createVenue(
//        @Header("Authorization")
//        token : String,
//        @Body requestCreateVenue: RequestCreateVenue
//    ) : ResponseCreateVenue
//
//    @POST("/venues/like")
//    suspend fun likeVenue(
//        @Header("Authorization")
//        token : String,
//        @Body requestLikeVenue: RequestLikeDislikeVenue
//    ) : ResponseLikeVenue
//
//    @DELETE("/venues/like")
//    suspend fun dislikeVenue(
//        @Header("Authorization")
//        token : String,
//        @Body requestDislikeVenue: RequestLikeDislikeVenue
//    ) : ResponseDislikeVenue
//

    @GET("/venues/{venue_id}/comment")
    suspend fun getVenueComments(
        @Path("venue_id")
        venueId: Int
    ) : Response<List<ResponseVenueComments>>

    @POST("/venues/comment")
    suspend fun addCommentVenue(
        @Header("Authorization")
        token : String,
        @Body
        requestAddCommentVenue: RequestAddCommentVenue
    )

//    @GET("/venues/comment/{comment_id}")
//    suspend fun getCommentDetails(
//        @Path("comment_id") comment_id: Int
//    ) : ResponseCommentDetails


//    @DELETE("/venues/comment")
//    suspend fun deleteCommentVenue(
//        //@Header("Authentication") bearer : "salam",
//        @Body requestDeleteCommentVenue: RequestDeleteCommentVenue
//    ) : ResponseDeleteCommentVenue
//
//
//    @GET("/venues/search/{query}")
//    suspend fun searchVenue(
//        @Path("query") query: String
//    ) : ResponseVenueSearch
}