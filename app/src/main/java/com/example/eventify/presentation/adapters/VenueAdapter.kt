package com.example.eventify.presentation.adapters

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.eventify.R
import com.example.eventify.common.base.BaseAdapter
import com.example.eventify.databinding.SampleVenueBinding
import com.example.eventify.domain.model.VenueItem

class VenueAdapter(
    val onClick: (VenueItem) -> Unit
) : BaseAdapter<SampleVenueBinding>(SampleVenueBinding::inflate) {

    var venues: List<VenueItem> = emptyList()

    override fun getItemCount(): Int {
        return venues.size
    }

    override fun onBindLight(binding: SampleVenueBinding, position: Int) {
        val venue = venues[position]
        with(binding) {
            textVenueName.text = venue.name
            Glide.with(imageVenue)
                .load(venue.imageLink)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageVenue)
            textVenueDescription.text = venue.description

            root.setOnClickListener {
                onClick(venue)
            }
        }
    }

    fun updateAdapter(newMovies: List<VenueItem>) {
        venues = newMovies
        notifyDataSetChanged()
    }
}