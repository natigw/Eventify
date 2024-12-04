package com.example.eventify.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceCoordinates(
    val id: Int,
    val name: String,
    val placeType: String,
    val long: String,
    val lat: String
) : Parcelable
