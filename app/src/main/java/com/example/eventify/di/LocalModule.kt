package com.example.eventify.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context : Context) : Context = context

    @Named("OnBoardingWelcome")
    @Provides
    fun provideSharedPrefOnBoardWelcome(context: Context) : SharedPreferences {
        return context.getSharedPreferences("onBoardingWelcome", Context.MODE_PRIVATE)
    }

    @Named("UserLoggedIn")
    @Provides
    fun provideSharedPrefUserLoggedIn(context: Context) : SharedPreferences {
        return context.getSharedPreferences("userLoggedIn", Context.MODE_PRIVATE)
    }

    @Named("UserDetails")
    @Provides
    fun provideSharedPrefAccessToken(context : Context) : SharedPreferences {
        return context.getSharedPreferences("userDetails",Context.MODE_PRIVATE)
    }



}