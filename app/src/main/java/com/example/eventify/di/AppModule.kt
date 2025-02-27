package com.example.eventify.di

import android.content.Context
import com.example.common.utils.NetworkConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context : Context) : Context = context

    @Provides
    @Singleton
    fun provideNetworkConnection(@ApplicationContext context: Context): NetworkConnection {
        return NetworkConnection(context)
    }

}