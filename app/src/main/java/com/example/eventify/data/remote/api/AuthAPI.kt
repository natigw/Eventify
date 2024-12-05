package com.example.eventify.data.remote.api

import com.example.eventify.data.remote.model.register.RequestUserRegistration
import com.example.eventify.data.remote.model.register.ResponseUserRegistration
import com.example.eventify.data.remote.model.userToken.RequestPasswordReset
import com.example.eventify.data.remote.model.userToken.RequestResendVerification
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
import retrofit2.http.Query

interface AuthAPI {
    @POST("/auth")
    suspend fun registerUser(
        @Body requestUserRegistration: RequestUserRegistration
    ): Response<ResponseUserRegistration>


    @FormUrlEncoded
    @POST("/auth/token")
    suspend fun loginUser(
        @Field("grant_type") grantType: String? = null,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("scope") scope: String? = null,
        @Field("client_id") clientId: String? = null,
        @Field("client_secret") clientSecret: String? = null
    ): Response<ResponseSuccessfulUserToken>


    @GET("/auth/verify-token")
    suspend fun verifyUserToken(
        @Header("Authorization") token : String
    ): Response<ResponseVerifyToken>


    @GET("/auth/token/refresh")
    suspend fun refreshAccessToken(
        @Query("refresh_token") refreshToken : String
    ) : Response<ResponseSuccessfulUserToken>

    @GET("/auth/confirm-email/{access_token}")
    suspend fun confirmEmail(
        @Path("access_token") accessToken : String
    ) : Response<String>


    @POST("/auth/resend-verification")
    suspend fun resendVerification(
        @Body requestResendVerification: RequestResendVerification
    ) : Response<String>

    @POST("/auth/password-reset-request")
    suspend fun requestPasswordReset(
        @Body requestPasswordReset: RequestPasswordReset
    ) : Response<String>





//    @GET("/social_auth/google/login")
//    suspend fun authGoogleLogin() : ResponseGoogleLogin
//
//    @GET("/social_auth/google/callback")
//    suspend fun authGoogleCallback() : ResponseGoogleCallback

}