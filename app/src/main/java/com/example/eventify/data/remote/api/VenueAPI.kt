package com.example.eventify.data.remote.api

import com.example.eventify.data.remote.model.venues.ResponseAllVenues
import retrofit2.Response
import retrofit2.http.GET




interface VenueAPI {
    @GET("/venues")
    suspend fun getAllVenues() : Response<List<ResponseAllVenues>>

//    @GET("/venues/{venue_id}")
//    suspend fun getVenueDetails(
//        @Path("venue_id") venue_id: Int
//    ) : ResponseVenueDetails
//
//    @POST("/venues")
//    suspend fun createVenue(
//        //@Header("Authentication") bearer : "salam",
//        @Body requestCreateVenue: RequestCreateVenue
//    ) : ResponseCreateVenue
//
//    @POST("/venues/like")
//    suspend fun likeVenue(
//        //@Header("Authentication") bearer : "salam",
//        @Body requestLikeVenue: RequestLikeDislikeVenue
//    ) : ResponseLikeVenue
//
//    @DELETE("/venues/like")
//    suspend fun dislikeVenue(
//        //@Header("Authentication") bearer : "salam",
//        @Body requestDislikeVenue: RequestLikeDislikeVenue
//    ) : ResponseDislikeVenue
//
//
//    @GET("/venues/{venue_id}/comment")
//    suspend fun getVenueComments(
//        @Path("venue_id") venue_id: Int
//    ) : ResponseVenueComments
//
//    @GET("/venues/comment/{comment_id}")
//    suspend fun getCommentDetails(
//        @Path("comment_id") comment_id: Int
//    ) : ResponseCommentDetails
//
//    @POST("/venues/comment")
//    suspend fun addCommentVenue(
//        //@Header("Authentication") bearer : "salam",
//        @Body requestAddCommentVenue: RequestAddCommentVenue
//    ) : ResponseAddCommentVenue
//
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