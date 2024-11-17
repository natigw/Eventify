package com.example.eventify.di

import com.example.eventify.data.remote.repository.VenueRepositoryImpl
import com.example.eventify.domain.repository.VenueRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
abstract class BindModule {

    @Binds
    abstract fun bindMovieRepository(impl: VenueRepositoryImpl): VenueRepository

}
