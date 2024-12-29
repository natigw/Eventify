package com.example.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EventItem(
    val eventId : Int,
    val name : String,
    val imageLink : String?,
    val eventDateTime: String
) : Parcelable