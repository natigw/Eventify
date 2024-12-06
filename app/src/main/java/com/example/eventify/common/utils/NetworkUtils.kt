package com.example.eventify.common.utils

object NetworkUtils {

    suspend fun <T>handleResponse(request : suspend ()->retrofit2.Response<T>) : T{
        try {
            val response = request()

            if(response.isSuccessful && response.body() != null){
                return response.body()!!
            }
            else{
                throw Exception("Error ${response.code()}: ${response.errorBody()?.string()}")
            }
        }
        catch (e : Exception){
            throw e
        }
    }

}