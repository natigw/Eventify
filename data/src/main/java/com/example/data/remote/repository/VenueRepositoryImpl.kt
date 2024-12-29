package com.example.data.remote.repository

import android.util.Log
import com.example.common.utils.functions.randomDouble
import com.example.common.utils.functions.roundDouble
import com.example.data.remote.api.VenueAPI
import com.example.data.remote.model.venues.comment.addComment.RequestAddVenueComment
import com.example.domain.model.AddCommentItem
import com.example.domain.model.CommentItem
import com.example.domain.model.VenueDetailsItem
import com.example.domain.model.VenueItem
import com.example.domain.repository.VenueRepository
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
                        venueId = it.id,
                        title = it.name,
                        imageLink = it.image1Link,
                        description = it.description
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
                    venueType = rawData.venueType.replace('_', ' ').replaceFirstChar { it.uppercaseChar() },
                    openHours = if (rawData.workHoursOpen.substring(0, 5) == rawData.workHoursClose.substring(0, 5)) "24 hours open" else "${rawData.workHoursOpen.substring(0, 5)} - ${rawData.workHoursClose.substring(0, 5)}",
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

//        override suspend fun getVenueCommentDetails(commentId: Int): ResponseVenueCommentDetails {
//        TODO("Not yet implemented")
//    }

    override suspend fun addVenueComment(requestAddVenueComment: AddCommentItem) {
        api.addVenueComment(RequestAddVenueComment(
            content = requestAddVenueComment.content,
            venue = requestAddVenueComment.placeId
        ))
        //TODO -> successful olub olmadigini check ele
    }

//    override suspend fun deleteVenueComment(
//        token: String,
//        requestDeleteVenueComment: RequestDeleteVenueComment
//    ) {
//        TODO("Not yet implemented")
//    }

}