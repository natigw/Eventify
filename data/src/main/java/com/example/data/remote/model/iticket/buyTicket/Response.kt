package com.example.data.remote.model.iticket.buyTicket


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("events")
    val events: List<Event>,
    @SerializedName("venues")
    val venues: List<Any>
)