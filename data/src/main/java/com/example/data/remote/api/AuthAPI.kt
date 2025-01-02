package com.example.data.remote.api

import com.example.data.remote.model.register.RequestUserRegistration
import com.example.data.remote.model.register.ResponseUserRegistration
import com.example.data.remote.model.register.UserData
import com.example.domain.model.auth.RequestResendVerification
import com.example.data.remote.model.userToken.RequestResetPassword
import com.example.data.remote.model.userToken.ResponseSuccessfulUserToken
import com.example.data.remote.model.userToken.ResponseVerifyToken
import com.example.data.remote.thirdpartyregister.RequestSignInWithGoogle
import com.example.domain.model.auth.SuccessfulUserTokenItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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

    @POST("/auth/token/refresh") // qardasimsan bu niye postdu
    suspend fun refreshAccessToken(
        @Query("refresh_token") refreshToken : String
    ) : Response<ResponseSuccessfulUserToken>


    @GET("/auth/verify-token")
    suspend fun verifyUserToken(
        @Header("Authorization") token : String
    ): Response<ResponseVerifyToken>

    @POST("/auth/resend-verification")
    suspend fun resendVerification(
        @Body requestResendVerification: RequestResendVerification
    )

    @POST("/auth/password-reset-request")
    suspend fun resetUserPassword(
        @Body requestResetPassword: RequestResetPassword
    )

    @POST("/social_auth/google")
    suspend fun signInWithGoogle(
        @Body requestSignInWithGoogle : RequestSignInWithGoogle
    ) : Response<SuccessfulUserTokenItem>

    @POST("/auth/user")
    suspend fun getUserData() : Response<UserData>

}