package com.example.eventify.data.remote.api

import com.example.eventify.data.remote.model.register.RequestUserRegistration
import com.example.eventify.data.remote.model.register.ResponseUserRegistration
import com.example.eventify.data.remote.model.userToken.RequestUserToken
import com.example.eventify.data.remote.model.userToken.ResponseSuccessfulUserToken
import com.example.eventify.data.remote.model.userToken.ResponseVerifyToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthAPI {
    @POST("/auth")
    suspend fun registerUser(
        @Body requestUserRegistration: RequestUserRegistration
    ): Response<ResponseUserRegistration>



    // sorus aqsinden
    @FormUrlEncoded
    @POST("/auth/token")
    suspend fun requestUserToken(
        @Field("grant_type") grantType: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("scope") scope: String? = null,
        @Field("client_id") clientId: String? = null,
        @Field("client_secret") clientSecret: String? = null
    ): Response<ResponseSuccessfulUserToken>


    @GET("/auth/verify-token/{token}")
    suspend fun verifyUserToken(
        @Path("token") token: String,
    ): ResponseVerifyToken

//    @GET("/social_auth/google/login")
//    suspend fun authGoogleLogin() : ResponseGoogleLogin
//
//    @GET("/social_auth/google/callback")
//    suspend fun authGoogleCallback() : ResponseGoogleCallback

}