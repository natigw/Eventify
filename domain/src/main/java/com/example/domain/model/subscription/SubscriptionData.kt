package com.example.domain.model.subscription

data class SubscriptionData(
    val name: String,
    val features: String,
    val priceMonthly: Double,
    val priceAnnually: Double
)