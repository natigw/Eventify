package com.example.domain.repository

import com.example.domain.model.EventDetailsItem
import com.example.domain.model.EventItem

interface EventRepository {
    suspend fun getEvents(): List<EventItem>
    suspend fun getEventDetails(eventId: Int): EventDetailsItem?
}