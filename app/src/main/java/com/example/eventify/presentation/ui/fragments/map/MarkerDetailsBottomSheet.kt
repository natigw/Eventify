package com.example.eventify.presentation.ui.fragments.map

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.eventify.R
import com.example.common.base.BaseBottomSheetFragment
import com.example.common.utils.nancyToastInfo
import com.example.common.utils.nancyToastWarning
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
import java.net.URI
import javax.inject.Inject

@AndroidEntryPoint
class MarkerDetailsBottomSheet : BaseBottomSheetFragment<BottomsheetMarkerDetailsBinding>(BottomsheetMarkerDetailsBinding::inflate) {

    private val viewModel by viewModels<MarkerDetailsViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val args by navArgs<MarkerDetailsBottomSheetArgs>()

    override fun onViewCreatedLight() {
        checkIfVenueOrEvent()
        observer()
        routeIntent()
    }

    private fun routeIntent() {
        binding.buttonShortestRouteMarkerDetails.setOnClickListener {
            val latitude = args.lat
            val longitude = args.lng
            val geoUri = "geo:$latitude,$longitude?q=$latitude,$longitude"

            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(geoUri)
            }

            if (intent.resolveActivity(requireActivity().packageManager) != null)
                requireActivity().startActivity(intent)
            else
                nancyToastWarning(requireContext(), getString(R.string.no_apps_to_open_maps))
        }
    }

    private fun observer(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.eventDetails
                .filter { it!=null }
                .collectLatest {
                    setEventUI(it!!)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.venueDetails
                .filter { it!=null }
                .collectLatest {
                    setVenueUI(it!!)
            }
        }
    }

    private fun checkIfVenueOrEvent() {
        if(args.placeType == "venue")
            viewModel.getVenueDetails(args.placeId)
        else
            viewModel.getEventDetails(args.placeId)
    }

    private fun setEventUI(eventDetails : EventDetailsItem){
        with(binding) {
            textVenueNameMarkerDetails.text = eventDetails.title
            textVenueDetailsMarkerDetailsText.text = getString(R.string.event_details)
            textVenueDescriptionMarkerDetails.text = eventDetails.description
            textVenueTypeMarkerDetailsText.text = getString(R.string.event_type)
            textVenueTypeMarkerDetails.text = eventDetails.eventType
            textVenueOpenHoursMarkerDetailsText.text = getString(R.string.event_duration_colon)
            textVenueOpenHoursMarkerDetails.text = eventDetails.eventDurationHours
            ratingBarMarkerDetails.rating = eventDetails.rating.toFloat()
            textVenueLikeCountMarkerDetails.text = eventDetails.likeCount.toString()
            Glide.with(imageVenueMarkerDetails)
                .load(eventDetails.imageLinks[0]) //TODO -> bunu multiple image et instagram kimi
                .placeholder(R.drawable.placeholder_venue)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageVenueMarkerDetails)
            buttonBuyTicketMarkerDetails.visibility = View.VISIBLE
            buttonShortestRouteMarkerDetails.text = getString(R.string.get_route)
            buttonBuyTicketMarkerDetails.setOnClickListener {
                nancyToastInfo(requireContext(), "navigating to iticket.az") //TODO -> bunu browserde acilan et
            }
        }
    }

    private fun setVenueUI(venueDetails : VenueDetailsItem){
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
        }
    }
}