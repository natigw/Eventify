package com.example.eventify.di

import com.example.eventify.data.remote.api.EventifyAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitClient(): Retrofit {
        return Retrofit
            .Builder()
            //.baseUrl("https://d6d0-62-217-154-197.ngrok-free.app")
            .baseUrl("http://10.0.2.2:8000")  //TODO -> buna bir care tap
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideEventifyApi(client: Retrofit): EventifyAPI {
        return client.create(EventifyAPI::class.java)
    }

}