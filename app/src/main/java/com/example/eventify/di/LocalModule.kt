package com.example.eventify.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {

    @Named("OnBoardingWelcome")
    @Provides
    fun provideSharedPrefOnBoardWelcome(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences("onBoardingWelcome", Context.MODE_PRIVATE)
    }

    @Named("UserLoggedIn")
    @Provides
    fun provideSharedPrefUserLoggedIn(@ApplicationContext context: Context) : SharedPreferences {
        return context.getSharedPreferences("userLoggedIn", Context.MODE_PRIVATE)
    }
}