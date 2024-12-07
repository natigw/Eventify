package com.example.eventify.presentation.ui.fragments.map

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.eventify.R
import com.example.eventify.common.base.BaseBottomSheetFragment
import com.example.eventify.common.utils.NancyToast
import com.example.eventify.data.remote.api.VenueAPI
import com.example.eventify.databinding.BottomsheetMarkerDetailsBinding
import com.example.eventify.domain.model.VenueDetailsItem
import com.example.eventify.domain.repository.VenueRepository
import com.example.eventify.presentation.viewmodels.MarkerDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MarkerDetailsBottomSheet : BaseBottomSheetFragment<BottomsheetMarkerDetailsBinding>(BottomsheetMarkerDetailsBinding::inflate) {

    private val args by navArgs<MarkerDetailsBottomSheetArgs>()

    @Inject
    lateinit var venueDetailsRepository: VenueRepository
    override fun onViewCreatedLight() {
        val placeId = args.placeId
        lifecycleScope.launch {
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