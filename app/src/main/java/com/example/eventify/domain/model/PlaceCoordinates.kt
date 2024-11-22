package com.example.eventify.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceCoordinates(
    val name: String,
    val long: String,
    val lat: String
) : Parcelable
