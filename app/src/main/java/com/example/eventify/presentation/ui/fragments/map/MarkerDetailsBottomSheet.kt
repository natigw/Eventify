package com.example.eventify.presentation.ui.fragments.map

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.eventify.R
import com.example.eventify.common.base.BaseBottomSheetFragment
import com.example.eventify.databinding.BottomsheetMarkerDetailsBinding
import com.example.eventify.domain.repository.EventRepository
import com.example.eventify.domain.repository.VenueRepository
import com.example.eventify.presentation.viewmodels.SharedViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MarkerDetailsBottomSheet : BaseBottomSheetFragment<BottomsheetMarkerDetailsBinding>(BottomsheetMarkerDetailsBinding::inflate) {

    private val args by navArgs<MarkerDetailsBottomSheetArgs>()

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    @Inject
    lateinit var venueDetailsRepository: VenueRepository
    @Inject
    lateinit var eventDetailsRepository: EventRepository

    override fun onViewCreatedLight() {
        val placeId = args.placeId
        lifecycleScope.launch {
            if (args.placeType == "venue") {
                val details = venueDetailsRepository.getVenueDetails(placeId)
                details?.let {
                    with(binding) {
                        textVenueNameMarkerDetails.text = details.title
                        textVenueDescriptionMarkerDetails.text = details.description
                        textVenueTypeMarkerDetails.text = details.venueType
                        textVenueOpenHoursMarkerDetails.text = details.openHours
                        ratingBarMarkerDetails.rating = details.rating.toFloat()
                        textVenueLikeCountMarkerDetails.text = details.likeCount.toString()
                        Glide.with(imageVenueMarkerDetails)
                            .load(details.imageLinks[0]) //TODO -> bunu multiple image et instagram kimi
                            .placeholder(R.drawable.placeholder_venue)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(imageVenueMarkerDetails)
                        buttonShortestRouteMarkerDetails.setOnClickListener {
                            lifecycleScope.launch {
                                sharedViewModel.setRouteCoordinates(details.coordinates)
                            }
                        }
                    }
                }
            } else {
                val eventDetails = eventDetailsRepository.getEventDetails(placeId)
                eventDetails?.let {
                    with(binding) {
                        textVenueNameMarkerDetails.text = eventDetails.title
                        textVenueDescriptionMarkerDetails.text = eventDetails.description
                        textVenueTypeMarkerDetails.text = eventDetails.eventType
                        VenueOpenHoursMarkerDetailsText.text = "Event duration:"
                        ratingBarMarkerDetails.rating = eventDetails.rating.toFloat()
                        textVenueLikeCountMarkerDetails.text = eventDetails.likeCount.toString()
                        Glide.with(imageVenueMarkerDetails)
                            .load(eventDetails.imageLinks[0]) //TODO -> bunu multiple image et instagram kimi
                            .placeholder(R.drawable.placeholder_venue)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(imageVenueMarkerDetails)
                        buttonBuyTicketMarkerDetails.visibility = View.VISIBLE
                        buttonShortestRouteMarkerDetails.text = "Get route"
                        buttonShortestRouteMarkerDetails.setOnClickListener {
                            lifecycleScope.launch {
                                sharedViewModel.setRouteCoordinates(eventDetails.coordinates)
                            }
                        }
                    }
                }
            }
        }
    }


//    private val viewmodel by viewModels<MarkerDetailsViewModel>()

//    override fun onViewCreatedLight() {
//        val placeId = args.placeId
//        lifecycleScope.launch {
//            viewmodel.venueDetails.collect { venueDetails ->
//                venueDetails?.let {
//                    bindDetails(it)
//                }
//            }
//        }
//    }
//    private fun bindDetails(details: VenueDetailsItem) {
//        with(binding) {
//            textVenueNameMarkerDetails.text = details.title
//            textVenueDescriptionMarkerDetails.text = details.description
//
//        }
//    }
}