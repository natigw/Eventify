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
            .baseUrl("https://83f8-62-217-156-0.ngrok-free.app/")
            //.baseUrl("http://10.0.2.2:8000")  //TODO -> bunu duzelt
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideEventifyApi(client: Retrofit): EventifyAPI {
        return client.create(EventifyAPI::class.java)
    }

}