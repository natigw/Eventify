package com.example.eventify.di

import android.content.Context
import android.content.SharedPreferences
import android.media.session.MediaSession.Token
import com.example.eventify.common.utils.AppUtils
import com.example.eventify.data.remote.interceptor.AuthInterceptor
import com.example.eventify.data.remote.interceptor.TokenManager
import com.example.eventify.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AuthModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor) : OkHttpClient{
        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideTokenManager(
        @Named("UserTokens")
        sharedPreferences: SharedPreferences,
        authRepository: AuthRepository
    ) : TokenManager{
        return TokenManager(
            sharedPreferences,
            authRepository
        )
    }

}