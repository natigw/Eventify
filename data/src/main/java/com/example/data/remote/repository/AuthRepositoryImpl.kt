package com.example.data.remote.repository

import android.util.Log
import com.example.data.remote.api.AuthAPI
import com.example.data.remote.model.register.RequestUserRegistration
import com.example.data.remote.model.userToken.RequestResetPassword
import com.example.data.remote.thirdpartyregister.RequestSignInWithGoogle
import com.example.domain.model.auth.RequestResendVerification
import com.example.domain.model.auth.SuccessfulUserTokenItem
import com.example.domain.model.auth.UserData
import com.example.domain.model.auth.UserRegistrationItem
import com.example.domain.repository.AuthRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    val api: AuthAPI
) : AuthRepository {

    override suspend fun registerUser(
        firstname: String,
        lastname: String,
        username: String,
        email: String,
        password: String
    ): UserRegistrationItem {
        try {
            val response = api.registerUser(
                RequestUserRegistration(
                    username = username,
                    email = email,
                    password = password,
                    firstName = firstname,
                    lastName = lastname,
                    isOrganizer = 0
                )
            )
            if(response.isSuccessful && response.body() != null){
                return UserRegistrationItem(
                    message = response.body()!!.message,
                    status = response.body()!!.status
                )
            }
            else{
                throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
            }

        }
        catch (e: Exception){
            throw e
        }
    }

    override suspend fun loginUser(
        username: String,
        password: String
    ) : SuccessfulUserTokenItem {
        try {
            val response = api.loginUser(
                username = username,
                password = password)

            if(response.isSuccessful && response.body() != null){
                val accessToken = response.body()!!.accessToken
                val refreshToken =response.body()!!.refreshToken
                val tokenType = response.body()!!.tokenType

                return SuccessfulUserTokenItem(
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    tokenType = tokenType
                )
            }
            else{
                throw Exception(response.errorBody()?.string())
            }
        }
        catch (e : Exception){
            throw e
        }
    }

    override suspend fun verifyUserToken(): Boolean {
        try {
            val response = api.verifyUserToken()

            if(response.isSuccessful && response.body() != null){
                   return response.body()!!.isAuthenticated
            }
            return false
        }
        catch (e : Exception){
            throw e
        }
    }

    override suspend fun refreshAccessToken(
        refreshToken: String
    ): SuccessfulUserTokenItem {
        try {
            val response = api.refreshAccessToken(refreshToken)

            if(response.isSuccessful && response.body() != null){
                return SuccessfulUserTokenItem(
                    accessToken = response.body()!!.accessToken,
                    refreshToken = response.body()!!.refreshToken,
                    tokenType = response.body()!!.tokenType
                )
            }
            else{
                throw Exception()
            }
        }
        catch (e : Exception){
            throw e
        }
    }

    override suspend fun resetUserPassword(userEmail: String): Boolean {
        return try {
            val response = api.resetUserPassword(RequestResetPassword(userEmail))
            true
        }
        catch (e : Exception){
            false
        }
    }

    override suspend fun signInWithGoogle() : SuccessfulUserTokenItem {
        val currentUser = Firebase.auth.currentUser
        if(currentUser != null){
            try {
                val fullName = currentUser.displayName.toString()
                val firstname = fullName.substringBefore(' ')
                val lastname =  if(fullName.contains(' ')) fullName.substringAfter(' ') else ""
                val userId = currentUser.uid.toString()
                val imageURL = currentUser.photoUrl.toString()
                val email = currentUser.email.toString()
                val provider = "google"

                Log.e("Data",
                    RequestSignInWithGoogle(
                    displayName = fullName,
                    email = email,
                    firstName = firstname,
                    lastName = lastname,
                    id = userId,
                    picture = imageURL,
                    provider = provider
                ).toString())

                val response = api.signInWithGoogle(
                    RequestSignInWithGoogle(
                        displayName = fullName,
                        email = email,
                        firstName = firstname,
                        lastName = lastname,
                        id = userId,
                        picture = imageURL,
                        provider = provider
                    )
                )

                if(response.isSuccessful && response.body()!=null){
                    Log.e("tester",response.body().toString())
                    val accessToken = response.body()!!.accessToken
                    val refreshToken =response.body()!!.refreshToken
                    val tokenType = response.body()!!.tokenType

                    return SuccessfulUserTokenItem(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        tokenType = tokenType
                    )
                }
                else{
                    throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
                }


            }catch (e:Exception){
                e.printStackTrace()
                throw e
            }
        }
        throw IllegalStateException("User not signed in!")
    }

    override suspend fun resendVerification(userEmail: String) : Boolean {
        return try {
            api.resendVerification(RequestResendVerification(userEmail))
            true
        }
        catch (e : Exception){
            throw e
        }
    }

    override suspend fun isUserVerified(userEmail: String): Boolean {
        try {
            val response = api.isUserVerified(userEmail)
            if(response.isSuccessful && response.body() != null){
                return response.body()!!
            }
            else{
                throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
            }

        }catch (e :Exception){
            throw e
        }
    }

    override suspend fun getUserData(): UserData {
        try {
            val response = api.getUserData()
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                return UserData(
                    username = responseBody.username,
                    fullName = "${responseBody.firstName} ${responseBody.lastName}",
                    isOrganizer = responseBody.isOrganizer
                )
            } else {
                throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
            }
        } catch (e :Exception){
            throw e
        }
    }
}