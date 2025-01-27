package com.example.data.remote.model.iticket.buyTicket


import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("age_limit")
    val ageLimit: String,
    @SerializedName("available_tickets_count")
    val availableTicketsCount: Int,
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("category_slug")
    val categorySlug: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("event_ends_at")
    val eventEndsAt: String,
    @SerializedName("event_starts_at")
    val eventStartsAt: String,
    @SerializedName("external_url")
    val externalUrl: Any,
    @SerializedName("id")
    val id: Int,
    @SerializedName("max_price")
    val maxPrice: Int,
    @SerializedName("min_price")
    val minPrice: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("poster_bg_url")
    val posterBgUrl: String,
    @SerializedName("poster_url")
    val posterUrl: String,
    @SerializedName("poster_wide_bg_url")
    val posterWideBgUrl: String,
    @SerializedName("poster_wide_url")
    val posterWideUrl: String,
    @SerializedName("public_state")
    val publicState: Int,
    @SerializedName("sell_ends_at")
    val sellEndsAt: Any,
    @SerializedName("sell_starts_at")
    val sellStartsAt: String,
    @SerializedName("slug")
    val slug: String,
    @SerializedName("upcoming_mode")
    val upcomingMode: Any,
    @SerializedName("venues")
    val venues: List<Venue>,
    @SerializedName("web_view_rotate")
    val webViewRotate: Boolean
)