package com.example.domain.model.places.event

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventItem(
    val eventId : Int,
    val name : String,
    val imageLink : String?,
    val eventDateTime: String,
    val lat : String,
    val lng : String
) : Parcelable