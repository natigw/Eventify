package com.example.eventify.di

import android.content.Context
import android.content.SharedPreferences
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
    fun provideTokenManager(
        authRepository: AuthRepository,
        @Named("UserTokens")
        sharedPreferences: SharedPreferences
    ) : TokenManager {
        return TokenManager(
            authRepository = authRepository,
            sharedPrefUserTokens = sharedPreferences
        )
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(
        context : Context,
        @Named("UserTokens") sharedPreferences: SharedPreferences,
        tokenManager: TokenManager,
    ) : AuthInterceptor {
        return AuthInterceptor(sharedPrefUserTokens = sharedPreferences){
            AppUtils.handleLogout(context,tokenManager)
        }
    }


    @Singleton
    @Provides
    fun provideOkHttpClient(authInterceptor: AuthInterceptor) : OkHttpClient{
        return OkHttpClient
            .Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

}