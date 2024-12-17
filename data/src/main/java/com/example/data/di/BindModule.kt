package com.example.data.di

import com.example.data.remote.repository.AuthRepositoryImpl
import com.example.data.remote.repository.EventRepositoryImpl
import com.example.data.remote.repository.VenueRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.EventRepository
import com.example.domain.repository.VenueRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class BindModule {

    @Binds
    abstract fun bindVenueRepository(impl: VenueRepositoryImpl): VenueRepository

    @Binds
    abstract fun bindEventRepository(impl: EventRepositoryImpl): EventRepository

    @Binds
    abstract fun bindAuthRepository(impl : AuthRepositoryImpl) : AuthRepository

}