package com.example.data.remote.model.iticket.buyTicket


import com.google.gson.annotations.SerializedName

data class Venue(
    @SerializedName("id")
    val id: Int,
    @SerializedName("map_lat")
    val mapLat: String,
    @SerializedName("map_lng")
    val mapLng: String,
    @SerializedName("mobile")
    val mobile: Any,
    @SerializedName("name")
    val name: String,
    @SerializedName("phone")
    val phone: String
)