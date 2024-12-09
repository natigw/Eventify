package com.example.eventify.data.remote.repository

import android.util.Log
import com.example.eventify.common.utils.randomDouble
import com.example.eventify.common.utils.roundDouble
import com.example.eventify.data.remote.api.VenueAPI
import com.example.eventify.data.remote.model.venues.comment.addComment.RequestAddVenueComment
import com.example.eventify.data.remote.model.venues.comment.commentDetails.ResponseVenueCommentDetails
import com.example.eventify.data.remote.model.venues.comment.deleteComment.RequestDeleteVenueComment
import com.example.eventify.data.remote.model.venues.comment.venueComments.ResponseVenueComments
import com.example.eventify.domain.model.CommentItem
import com.example.eventify.domain.model.VenueDetailsItem
import com.example.eventify.domain.model.VenueItem
import com.example.eventify.domain.repository.VenueRepository
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class VenueRepositoryImpl @Inject constructor(
    private val api: VenueAPI
) : VenueRepository {

    override suspend fun getVenues(): List<VenueItem> {
        val response = api.getAllVenues()
        if(response.isSuccessful){
            response.body()?.let { rawData ->
                return rawData.map {
                    VenueItem(
                        placeId = it.id,
                        name = it.name,
                        imageLink = it.image1Link,
                        description = it.description,
                        venueType = it.venueType,
                        openHours = "${it.workHoursOpen.substring(0, 5)} - ${it.workHoursClose.substring(0, 5)}",
                        likeCount = it.numLikes,
                        latCoordinate = if (it.lat != "string") it.lat.toDouble() else 0.0,
                        lngCoordinate = if (it.lng != "string") it.lng.toDouble() else 0.0
                    )
                }
            }

        }

        Log.e("venueRequestError", response.errorBody().toString())
        return emptyList()
    }

    override suspend fun getVenueDetails(venueId: Int): VenueDetailsItem? {
        val response = api.getVenueDetails(venueId)
        if (response.isSuccessful) {
            response.body()?.let { rawData ->
                return VenueDetailsItem(
                    venueId = rawData.id,
                    title = rawData.name,
                    description = rawData.description,
                    imageLinks = listOf(rawData.image1Link, rawData.image2Link, rawData.image3Link),
                    venueType = rawData.venueType,
                    openHours = "${rawData.workHoursOpen.substring(0, 5)} - ${rawData.workHoursClose.substring(0, 5)}",
                    likeCount = rawData.numLikes,
                    rating = roundDouble(randomDouble(max = 5.0)),
                    coordinates = if (rawData.lat != "string") LatLng(rawData.lat.toDouble(), rawData.lng.toDouble()) else LatLng(0.0, 0.0)
                )
            }
        }

        Log.e("venueDetailsRequestError", response.errorBody().toString())
        return null
    }

    override suspend fun getVenueComments(venueId: Int): List<CommentItem> {
        val response = api.getVenueComments(venueId)
        if (response.isSuccessful) {
            response.body()?.let { rawComment ->
                return rawComment.map {
                    //val details = api.getVenueCommentDetails(it.id)  //heleki eyni datani qaytarir - useless<<
                    CommentItem(
                        commentId = it.id,
                        username = it.ownerId.toString(),
                        content = it.content,
                        date = "${it.createdAt.substring(0,10)}, ${it.createdAt.substring(11, 16)}"
                    )
                }
            }
        }
        Log.e("venueCommentRequestError", response.errorBody().toString())
        return emptyList()
    }

    override suspend fun getVenueCommentDetails(commentId: Int): ResponseVenueCommentDetails {
        TODO("Not yet implemented")
    }

    override suspend fun addVenueComment(
        token: String,
        requestAddVenueComment: RequestAddVenueComment
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteVenueComment(
        token: String,
        requestDeleteVenueComment: RequestDeleteVenueComment
    ) {
        TODO("Not yet implemented")
    }

}