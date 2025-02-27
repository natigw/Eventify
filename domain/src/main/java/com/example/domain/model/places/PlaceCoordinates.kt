package com.example.domain.model.places

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceCoordinates(
    val placeId: Int,
    val name: String,
    val placeType: String,
    val long: Double,
    val lat: Double
) : Parcelable
