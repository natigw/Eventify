package com.example.eventify.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VenueItem(
    val placeId : Int,
    val name : String,
    val imageLink : String?,
    val description : String,
    val venueType: String,
    val openHour: String,
    val closeHour: String,
    val likeCount: Int,
    val latCoordinate : Double,
    val lngCoordinate : Double
) : Parcelable