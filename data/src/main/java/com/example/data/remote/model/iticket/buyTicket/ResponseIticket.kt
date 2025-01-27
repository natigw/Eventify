package com.example.data.remote.model.iticket.buyTicket


import com.google.gson.annotations.SerializedName

data class ResponseIticket(
    @SerializedName("response")
    val response: Response,
    @SerializedName("result")
    val result: String,
    @SerializedName("status")
    val status: Int
)