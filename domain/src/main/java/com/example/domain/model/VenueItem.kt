package com.example.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VenueItem(
    val venueId : Int,
    val title : String,
    val imageLink : String?,
    val description : String
) : Parcelable