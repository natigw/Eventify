package com.example.eventify.common.utils


import kotlinx.coroutines.channels.Channel

object AppUtils {
    val authChannel = Channel<RequestChannel>()
}