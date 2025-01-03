package com.example.common.utils.functions

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun getLocalTime(): LocalDateTime {
    return LocalDateTime.now()
}

fun getLocalTimeFormattedYMDHMS(): String {
    val currentTime = getLocalTime()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val formattedTime = currentTime.format(formatter)
    return formattedTime
}

fun getInstantTime(): Instant {
    return Instant.now()
}

fun getInstantTimeConverted(): ZonedDateTime {
    val currentInstant = getInstantTime()
    val timeZone = ZoneId.systemDefault() //Get the system's default time zone
    val localTime = ZonedDateTime.ofInstant(currentInstant, timeZone) //Convert Instant to ZonedDateTime
    return localTime
}

fun getInstantTimeConvertedFormatted(): String {
    val localTime = getInstantTimeConverted()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z")
    val formattedLocalTime = localTime.format(formatter)
    return formattedLocalTime
}