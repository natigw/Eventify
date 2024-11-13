package com.example.eventify.data.remote.api

import com.example.eventify.data.remote.model.register.RequestUserRegistration
import com.example.eventify.data.remote.model.register.ResponseUserRegistration
import com.example.eventify.data.remote.model.userToken.RequestUserToken
import com.example.eventify.data.remote.model.userToken.ResponseSuccessfulUserToken
import com.example.eventify.data.remote.model.userToken.ResponseVerifyToken
import com.example.eventify.data.remote.model.venues.ResponseAllVenues
import com.example.eventify.data.remote.model.venues.addComment.RequestAddCommentVenue
import com.example.eventify.data.remote.model.venues.createVenue.RequestCreateVenue
import com.example.eventify.data.remote.model.venues.likeDislike.RequestLikeDislikeVenue
import com.example.eventify.data.remote.model.venues.deleteComment.RequestDeleteCommentVenue
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventifyAPI {

    @POST("/auth")
    suspend fun registerUser(
        @Body requestUserRegistration : RequestUserRegistration
    ) : ResponseUserRegistration

    @POST("/auth/token")
    suspend fun requestUserToken(
        @Body requestUserToken: RequestUserToken
    ) : ResponseSuccessfulUserToken

    @GET("/auth/verify-token/{token}")
    suspend fun verifyUserToken(
        @Path("token") token: String,
    ) : ResponseVerifyToken

//    @GET("/social_auth/google/login")
//    suspend fun authGoogleLogin() : ResponseGoogleLogin
//
//    @GET("/social_auth/google/callback")
//    suspend fun authGoogleCallback() : ResponseGoogleCallback
//
//
    @GET("/venues")
    suspend fun getAllVenues() : ResponseAllVenues

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