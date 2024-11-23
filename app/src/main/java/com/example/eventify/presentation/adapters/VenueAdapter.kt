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
            textVenueDescription.text = venue.description
            textVenueType.text = venue.venueType
            textVenueOpenHours.text =
                "${venue.openHour.substring(0, 5)} - ${venue.closeHour.substring(0, 5)}"
            textVenueLikeCount.text = venue.likeCount.toString()
            Glide.with(imageVenue)
                .load(venue.imageLink)
                .placeholder(R.drawable.placeholder_venue)
                .error(R.drawable.ic_launcher_foreground)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageVenue)

            var flag = true
            buttonLikeVenue.setOnClickListener {
                if (buttonLikeVenue.tag == "not_liked") {
                    buttonLikeVenue.setIconResource(R.drawable.like_fav)
                    textVenueLikeCount.text = (venue.likeCount + 1).toString()
                    buttonLikeVenue.tag = "liked"
                }
                else {
                    buttonLikeVenue.setIconResource(R.drawable.like_fav_border)
                    textVenueLikeCount.text = venue.likeCount.toString()
                    buttonLikeVenue.tag = "not_liked"
                }
//                buttonLikeVenue.setIconResource(if (flag) R.drawable.like_fav else R.drawable.like_fav_border)
//                flag = !flag
//                buttonLikeVenue.setIconTintResource(R.color.purple_light_eventify)
//                textVenueLikeCount.text = if (flag) (venue.likeCount + 1).toString() else venue.likeCount.toString()
                textVenueName.text = "Like olundu ama bunu duzzeltt"
                //TODO -> like olsun request atsin, icon qirmizi urek olsun, like count text bir dene artsin
            }
            buttonReadMoreVenues.setOnClickListener {
                //TODO -> button more duzelt next 5 lines, show less
            }
            buttonVenueShowLocation.setOnClickListener {
                //TODO -> button more duzelt next 5 lines, show less
                onClick(venue)
            }
        }
    }

//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }

    fun updateAdapter(newVenues: List<VenueItem>) {
        venues = newVenues
        notifyDataSetChanged()
    }
}