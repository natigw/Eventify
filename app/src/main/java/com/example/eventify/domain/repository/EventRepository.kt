package com.example.eventify.domain.repository

import com.example.eventify.domain.model.EventDetailsItem
import com.example.eventify.domain.model.EventItem

interface EventRepository {
    suspend fun getEvents(): List<EventItem>
    suspend fun getEventDetails(eventId: Int): EventDetailsItem?
}