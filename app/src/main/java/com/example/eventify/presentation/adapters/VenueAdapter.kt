package com.example.eventify.presentation.adapters

import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseAdapter
import com.example.domain.model.VenueItem
import com.example.eventify.R
import com.example.eventify.databinding.SampleVenueBinding

class VenueAdapter(
    val onClick: (VenueItem) -> Unit
) : BaseAdapter<SampleVenueBinding>(SampleVenueBinding::inflate) {

    var venues: List<VenueItem> = emptyList()

    override fun getItemCount(): Int {
        return venues.size
    }

    override fun onBindLight(binding: SampleVenueBinding, position: Int) {
        val venue = venues[position]
//        binding.venues = venue  // Bind the data
//        binding.executePendingBindings()  // Ensure immediate binding updates
        with(binding) {
            textVenueName.text = venue.title
            textVenueDescription.text = venue.description
            Glide.with(imageVenue)
                .load(venue.imageLink)
                .placeholder(R.drawable.placeholder_venue)

                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageVenue)
            Log.e("image",venue.imageLink.toString())

            buttonLikeVenue.setOnClickListener {
                if (buttonLikeVenue.tag == "not_liked") {
                    buttonLikeVenue.setIconResource(R.drawable.like_fav)
                    //TODO -> like request at ve notifyOnChange ile dinamik yoxla
                    buttonLikeVenue.tag = "liked"
                } else {
                    buttonLikeVenue.setIconResource(R.drawable.like_fav_border)
                    buttonLikeVenue.tag = "not_liked"
                }
                //TODO -> like olsun request atsin, icon fill olsun
            }

            root.setOnClickListener{
                onClick(venue)
            }
            buttonVenueAllDetails.setOnClickListener {
                onClick(venue)
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateAdapter(newVenues: List<VenueItem>) {
        venues = newVenues
        notifyDataSetChanged()
    }
}