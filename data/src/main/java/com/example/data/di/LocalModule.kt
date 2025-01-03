package com.example.data.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {
    @Named("OnBoardingWelcome")
    @Provides
    fun provideSharedPrefOnBoardWelcome(context: Context) : SharedPreferences {
        return context.getSharedPreferences("onBoardingWelcome", Context.MODE_PRIVATE)
    }

    @Named("UserTokens")
    @Provides
    fun provideSharedPrefTokens(context : Context) : SharedPreferences {
        return context.getSharedPreferences("userTokens",Context.MODE_PRIVATE)
    }

    @Named("LanguageChoice")
    @Provides
    fun provideSharedPrefLanguage(context : Context) : SharedPreferences {
        return context.getSharedPreferences("language",Context.MODE_PRIVATE)
    }
}