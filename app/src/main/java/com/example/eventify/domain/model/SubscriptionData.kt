package com.example.eventify.domain.model

data class SubscriptionData(
    val name: String,
    val features: String,
    val priceMonthly: Double,
    val priceAnnually: Double
)