package com.example.domain.model.places

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchItem(
    val placeId : Int,
    val name : String,
    val lat : String,
    val lng : String,
    val placeType : String
) : Parcelable