package com.example.eventify.di

import android.content.SharedPreferences
import com.example.eventify.data.remote.api.AuthAPI
import com.example.eventify.data.remote.api.EventAPI
import com.example.eventify.data.remote.api.VenueAPI
import com.example.eventify.data.remote.interceptor.TokenManager
import com.example.eventify.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl("https://eventify-az.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthApi(client: Retrofit): AuthAPI {
        return client.create(AuthAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideVenueApi(client: Retrofit): VenueAPI {
        return client.create(VenueAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideEventApi(client: Retrofit): EventAPI {
        return client.create(EventAPI::class.java)
    }




}