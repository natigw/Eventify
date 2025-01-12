package com.example.data.remote.repository

import android.util.Log
import com.example.common.utils.functions.dateFormatterIFYEAR_MNAMED_Comma_HM
import com.example.common.utils.functions.randomDouble
import com.example.common.utils.functions.roundDouble
import com.example.data.remote.api.VenueAPI
import com.example.data.remote.model.venues.comment.addComment.RequestAddVenueComment
import com.example.data.remote.model.venues.likeDislike.RequestLikeDislikeVenue
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.domain.model.places.event.EventItem
import com.example.domain.model.places.event.SearchEventItem
import com.example.domain.model.places.venue.VenueDetailsItem
import com.example.domain.model.places.venue.VenueItem
import com.example.domain.repository.VenueRepository
import com.google.android.gms.maps.model.LatLng
import java.time.Instant
import javax.inject.Inject

class VenueRepositoryImpl @Inject constructor(
    private val api: VenueAPI
) : VenueRepository {

    override suspend fun getVenues(): List<VenueItem> {
        try {
            val response = api.getAllVenues()
            if(response.isSuccessful && response.body()!=null){
                response.body()!!.let { rawData ->
                    return rawData.map {
                        VenueItem(
                            venueId = it.id,
                            title = it.name,
                            imageLink = it.image1Link,
                            description = it.description,
                            lat = it.lat,
                            lng = it.lng
                        )
                    }
                }
            }
            else{
                throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
            }
        }
        catch (e : Exception){
            throw e
        }
    }

    override suspend fun getVenueDetails(venueId: Int): VenueDetailsItem {
        try {
            val response = api.getVenueDetails(venueId)
            if (response.isSuccessful && response.body()!= null) {
                response.body()!!.let { rawData ->
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
            else{
                throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
            }
        }
        catch (e : Exception){
            throw e
        }
    }

    override suspend fun getVenueComments(venueId: Int): List<CommentItem> {

        try {
            val response = api.getVenueComments(venueId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.let { rawComment ->
                    return rawComment.map {
                        //val details = api.getVenueCommentDetails(it.id)  //heleki eyni datani qaytarir - useless<<
                        CommentItem(
                            commentId = it.id,
                            username = it.ownerId.toString(),
                            content = it.content,
                            date = dateFormatterIFYEAR_MNAMED_Comma_HM(Instant.parse("${it.createdAt}Z"))
                        )
                    }
                }
            }
            else{
                throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
            }

        }
        catch (e : Exception){
            throw e
        }
    }

    override suspend fun addVenueComment(requestAddVenueComment: AddCommentItem) {
        try {
            api.addVenueComment(RequestAddVenueComment(
                content = requestAddVenueComment.content,
                venue = requestAddVenueComment.placeId
            ))
        }
        catch (_: Exception){}
        //TODO -> successful olub olmadigini check ele
    }

//    override suspend fun deleteVenueComment(
//        token: String,
//        requestDeleteVenueComment: RequestDeleteVenueComment
//    ) {
//        TODO("Not yet implemented")
//    }

    override suspend fun likeVenue(venueId: Int) {
        try {
            api.likeVenue(
                RequestLikeDislikeVenue(
                    venue = venueId
                )
            )

        }
        catch (_:Exception){}
    }

    override suspend fun searchVenue(query: String): List<VenueItem> {
        try {
            val response = api.searchVenue(query)
            if(response.isSuccessful && response.body() != null){
                return response.body()!!.map{
                    VenueItem(
                        venueId = it.id,
                        title = it.name,
                        imageLink = it.image1Link,
                        description = it.description,
                        lat = it.lat,
                        lng = it.lng
                    )
                }
            }
            else{
                throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
            }
        }
        catch (e:Exception){
            throw e
        }
    }
}