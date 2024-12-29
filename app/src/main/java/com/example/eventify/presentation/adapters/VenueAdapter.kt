package com.example.eventify.presentation.adapters

import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseAdapter
import com.example.domain.model.VenueItem
import com.example.eventify.R
import com.example.eventify.databinding.SampleVenueBinding

//class VenueAdapter(
//    val onClickSeeComments: (VenueItem) -> Unit,
//    val onClickShowInMap: (VenueItem) -> Unit
//) : BaseAdapter<SampleVenueDetailsBinding>(SampleVenueDetailsBinding::inflate) {
//
//    var venues: List<VenueItem> = emptyList()
//
//    override fun getItemCount(): Int {
//        return venues.size
//    }
//
//    override fun onBindLight(binding: SampleVenueDetailsBinding, position: Int) {
//        val venue = venues[position]
////        binding.venues = venue  // Bind the data
////        binding.executePendingBindings()  // Ensure immediate binding updates
//        with(binding) {
//            textVenueName.text = venue.name
//            textVenueDescription.text = venue.description
//            textVenueType.text = venue.venueType
//            textVenueOpenHours.text = venue.openHours
//            textVenueLikeCount.text = venue.likeCount.toString()
//            Glide.with(imageVenue)
//                .load(venue.imageLink)
//                .placeholder(R.drawable.placeholder_venue)
//                .error(R.drawable.ic_launcher_foreground)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(imageVenue)
//
//            var flag = true
//            buttonLikeVenue.setOnClickListener {
//                if (buttonLikeVenue.tag == "not_liked") {
//                    buttonLikeVenue.setIconResource(R.drawable.like_fav)
//                    textVenueLikeCount.text = (venue.likeCount + 1).toString()
//                    buttonLikeVenue.tag = "liked"
//                } else {
//                    buttonLikeVenue.setIconResource(R.drawable.like_fav_border)
//                    textVenueLikeCount.text = venue.likeCount.toString()
//                    buttonLikeVenue.tag = "not_liked"
//                }
////                buttonLikeVenue.setIconResource(if (flag) R.drawable.like_fav else R.drawable.like_fav_border)
////                flag = !flag
////                buttonLikeVenue.setIconTintResource(R.color.purple_light_eventify)
////                textVenueLikeCount.text = if (flag) (venue.likeCount + 1).toString() else venue.likeCount.toString()
//                //TODO -> like olsun request atsin, icon fill olsun, like count text bir dene artsin
//            }
//
//            buttonReadMoreVenues.post {
//                val layout = textVenueDescription.layout
//                val lines = layout.lineCount
//                if (lines > 0) {
//                    if (layout.getEllipsisCount(lines - 1) > 0) {
//                        buttonReadMoreVenues.visibility = View.VISIBLE
//                    } else {
//                        buttonReadMoreVenues.visibility = View.GONE
//                    }
//                }
//            }
//            var flagRead = true
//            buttonReadMoreVenues.setOnClickListener {
//                if (flagRead) {
//                    textVenueDescription.maxLines = Integer.MAX_VALUE
//                    buttonReadMoreVenues.text = "Read less"
//                    flagRead = false
//                } else {
//                    buttonReadMoreVenues.text = "Read more"
//                    textVenueDescription.maxLines = 3
//                    flagRead = true
//                }
//            }
//
////            buttonReadMoreVenues.visibility = if (textVenueDescription.layout.lineCount < 3) View.GONE else View.VISIBLE
////            buttonReadMoreVenues.setOnClickListener {
////                if (textVenueDescription.layout.lineCount > 3 && textVenueDescription.maxLines < textVenueDescription.layout.lineCount) {
////                    textVenueDescription.maxLines += 3
////                }
////                if (buttonReadMoreVenues.text == "Read less") {
////                    textVenueDescription.maxLines = 3
////                    buttonReadMoreVenues.text = "Read more"
////                }
////                if (textVenueDescription.maxLines >= textVenueDescription.layout.lineCount) {
////                    buttonReadMoreVenues.text = "Read less"
////                }
////            }
//
//            buttonVenueSeeComments?.setOnClickListener {
//                onClickSeeComments(venue)
//            }
//            buttonVenueShowLocation.setOnClickListener {
//                onClickShowInMap(venue)
//            }
//        }
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }
//
//    fun updateAdapter(newVenues: List<VenueItem>) {
//        venues = newVenues
//        notifyDataSetChanged()
//    }
//}


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