package com.example.data.di

import android.content.SharedPreferences
import com.example.data.remote.interceptor.AuthInterceptor
import com.example.data.remote.interceptor.RetryInterceptor
import com.example.data.remote.interceptor.TokenManager
import com.example.domain.repository.AuthRepository
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
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        retryInterceptor: RetryInterceptor
    ) : OkHttpClient{
        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
//            .addInterceptor(retryInterceptor)
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