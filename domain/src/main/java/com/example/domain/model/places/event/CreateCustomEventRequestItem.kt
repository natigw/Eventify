package com.example.domain.model.places.event

import java.time.Instant
import java.time.OffsetTime

data class CreateCustomEventRequestItem(
    val title: String,
    val description: String,
    val eventType: EventType, //enum?
    val posterImageLink: String?,
    val date: Instant,  //or OffsetDateTime ??
    val start: OffsetTime,  //bulari da deqiqlesdir
    val finish: OffsetTime, //
    val lat: String, //double?
    val lng: String  //
)
