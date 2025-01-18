package com.example.eventify.presentation.ui.fragments.map

import android.net.Uri
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.eventify.R
import com.example.common.base.BaseBottomSheetFragment
import com.example.domain.model.places.event.EventDetailsItem
import com.example.domain.model.places.venue.VenueDetailsItem
import com.example.eventify.databinding.BottomsheetMarkerDetailsBinding
import com.example.domain.repository.EventRepository
import com.example.domain.repository.VenueRepository
import com.example.eventify.presentation.viewmodels.MarkerDetailsViewModel
import com.example.eventify.presentation.viewmodels.SharedViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MarkerDetailsBottomSheet : BaseBottomSheetFragment<BottomsheetMarkerDetailsBinding>(BottomsheetMarkerDetailsBinding::inflate) {

    private val args by navArgs<MarkerDetailsBottomSheetArgs>()

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val viewModel by viewModels<MarkerDetailsViewModel>()

    override fun onViewCreatedLight() {
        checkIfVenueOrEvent()
        observer()

    }

    fun observer(){
        lifecycleScope.launch {
            viewModel.eventDetails
                .filter { it!=null }
                .collectLatest {
                    setEventUI(it!!)
            }
        }

        lifecycleScope.launch {
            viewModel.venueDetails
                .filter { it!=null }
                .collectLatest {
                    setVenueUI(it!!)
            }
        }
    }

    fun checkIfVenueOrEvent(){
        if(args.placeType == "venue"){
            viewModel.getVenueDetails(args.placeId)
        }
        else {
            viewModel.getEventDetails(args.placeId)
        }
    }

    fun setEventUI(eventDetails : EventDetailsItem){
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
                    dismiss()
                }
            }
        }
    }

    fun setVenueUI(venueDetails : VenueDetailsItem){
        with(binding) {
            textVenueNameMarkerDetails.text = venueDetails.title
            textVenueDescriptionMarkerDetails.text = venueDetails.description
            textVenueTypeMarkerDetails.text = venueDetails.venueType
            textVenueOpenHoursMarkerDetails.text = venueDetails.openHours
            ratingBarMarkerDetails.rating = venueDetails.rating.toFloat()
            textVenueLikeCountMarkerDetails.text = venueDetails.likeCount.toString()
            Glide.with(imageVenueMarkerDetails)
                .load(venueDetails.imageLinks[0]) //TODO -> bunu multiple image et instagram kimi
                .placeholder(R.drawable.placeholder_venue)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageVenueMarkerDetails)
            buttonShortestRouteMarkerDetails.setOnClickListener {
                lifecycleScope.launch {
                    sharedViewModel.setRouteCoordinates(venueDetails.coordinates)
                    dismiss()
                }
            }
        }
    }



}