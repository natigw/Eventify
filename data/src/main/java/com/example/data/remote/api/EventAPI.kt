package com.example.data.remote.api

import com.example.data.remote.model.events.FileUploadGetLinkResponse
import com.example.data.remote.model.events.allEvents.ResponseAllEvents
import com.example.data.remote.model.events.comment.addComment.RequestAddEventComment
import com.example.data.remote.model.events.comment.commentDetails.ResponseEventCommentDetails
import com.example.data.remote.model.events.comment.eventComments.ResponseEventComments
import com.example.data.remote.model.events.createEvent.RequestCreateCustomEvent
import com.example.data.remote.model.events.eventDetails.ResponseEventDetails
import com.example.data.remote.model.events.likeDislike.RequestLikeDislikeEvent
import com.example.data.remote.model.events.likeDislike.favEvents.ResponseFavEventIDs
import com.example.data.remote.model.events.likeDislike.favEvents.ResponseFavEvents
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface EventAPI {
    @GET("/events")
    suspend fun getAllEvents() : Response<List<ResponseAllEvents>>

    @GET("/events/{event_id}")
    suspend fun getEventDetails(
        @Path("event_id") eventId: Int
    ) : Response<ResponseEventDetails>


//    @POST("/events")
//    suspend fun createEvent(
//        @Body requestCreateEvent: RequestCreateEvent
//    )

    @POST("/events/custom")
    suspend fun createCustomEvent(
        @Body
        requestCreateCustomEvent: RequestCreateCustomEvent
    ) //: Response<>

    @GET("/events/favorites")
    suspend fun getFavEvents() : Response<List<ResponseFavEvents>>

    @GET("/events/favorites-ids")
    suspend fun getFavEventIDs() : Response<List<ResponseFavEventIDs>>

    @POST("/events/like")
    suspend fun likeEvent(
        @Body
        requestLikeEvent: RequestLikeDislikeEvent
    )

    @DELETE("/events/like")
    suspend fun dislikeEvent(
        @Body
        requestDeleteLikeEvent : RequestLikeDislikeEvent
    )


    @GET("/events/{event_id}/comment")
    suspend fun getEventComments(
        @Path("event_id")
        eventId: Int
    ): Response<List<ResponseEventComments>>

    @GET("/events/comment/{comment_id}")
    suspend fun getEventCommentDetails(
        @Path("comment_id")
        commentId: Int
    ): ResponseEventCommentDetails

    @POST("/events/comment")
    suspend fun addEventComment(
        @Body
        requestAddEventComment: RequestAddEventComment
    )

//    @DELETE("/events/comment")
//    suspend fun deleteEventComment(
//        @Body
//        requestDeleteEventComment: RequestDeleteEventComment
//    )
//
//
//    @GET("/events/search/{query}")
//    suspend fun searchEvent(
//        @Path("query") query: String
//    ) : ResponseEventSearch

    @Multipart
    @POST("/files")
    suspend fun uploadFileAndGetLink(
        @Query("destination")
        destination: String, //= "events",
        @Part
        fileUploadBody: MultipartBody.Part
    ) : Response<FileUploadGetLinkResponse>
}