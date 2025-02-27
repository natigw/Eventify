package com.example.data.remote.api

import com.example.data.remote.model.iticket.buyTicket.ResponseIticket
import retrofit2.http.GET
import retrofit2.http.Query

interface ITicketAPI {

    @GET("search")
    suspend fun searchITicket(
        @Query("client")
        client: String,
        @Query("q")
        query: String
    ): ResponseIticket

}