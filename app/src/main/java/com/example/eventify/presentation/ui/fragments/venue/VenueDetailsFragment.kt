package com.example.eventify.presentation.ui.fragments.venue

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseFragment
import com.example.common.utils.functions.validateInputFieldEmpty
import com.example.common.utils.startShimmer
import com.example.common.utils.stopShimmer
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.PlaceCoordinates
import com.example.domain.model.places.venue.VenueDetailsItem
import com.example.eventify.R
import com.example.eventify.databinding.FragmentVenueDetailsBinding
import com.example.eventify.presentation.adapters.CommentAdapter
import com.example.eventify.presentation.viewmodels.SharedViewModel
import com.example.eventify.presentation.viewmodels.VenueDetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VenueDetailsFragment : BaseFragment<FragmentVenueDetailsBinding>(FragmentVenueDetailsBinding::inflate) {

    private val viewmodel by viewModels<VenueDetailsViewModel>()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args by navArgs<VenueDetailsFragmentArgs>()

    private val commentAdapter = CommentAdapter()
    
    override fun onResume() {
        super.onResume()
        if (viewmodel.venueDetails.value == null) {
            makeViewsInvisible()
            startShimmer(binding.shimmerVenueDetails)
        }
    }
    
    override fun onViewCreatedLight() {
        binding.textVenueName.isSelected = true
        updateAdapters()
        setAdapters()
        observer()
    }
    
    override fun onPause() {
        super.onPause()
        stopShimmer(binding.shimmerVenueDetails)
        makeViewsVisible()
    }

    private fun makeViewsInvisible(){
        with(binding) {
            textVenueName.visibility = View.INVISIBLE
            imageVenue.visibility = View.INVISIBLE
            textVenueDetailsText.visibility = View.INVISIBLE
            textVenueDescription.visibility = View.INVISIBLE
            textVenueTypeText.visibility = View.INVISIBLE
            textVenueType.visibility = View.INVISIBLE
            textVenueOpenHoursText.visibility = View.INVISIBLE
            textVenueOpenHours.visibility = View.INVISIBLE
            textVenueLikesText.visibility = View.INVISIBLE
            textVenueLikeCount.visibility = View.INVISIBLE
            buttonVenueShowLocation.visibility = View.INVISIBLE
            textVenueDetailsCommentsText.visibility = View.INVISIBLE
            textInputLayoutWriteCommentVenueDetails.visibility = View.INVISIBLE
            textInputAddCommentVenue.visibility = View.INVISIBLE
            buttonSendCommentVenueDetails.visibility = View.INVISIBLE
        }
    }
    private fun makeViewsVisible(){
        with(binding) {
            textVenueName.visibility = View.VISIBLE
            imageVenue.visibility = View.VISIBLE
            textVenueDetailsText.visibility = View.VISIBLE
            textVenueDescription.visibility = View.VISIBLE
            textVenueTypeText.visibility = View.VISIBLE
            textVenueType.visibility = View.VISIBLE
            textVenueOpenHoursText.visibility = View.VISIBLE
            textVenueOpenHours.visibility = View.VISIBLE
            textVenueLikesText.visibility = View.VISIBLE
            textVenueLikeCount.visibility = View.VISIBLE
            buttonVenueShowLocation.visibility = View.VISIBLE
            textVenueDetailsCommentsText.visibility = View.VISIBLE
            textInputLayoutWriteCommentVenueDetails.visibility = View.VISIBLE
            textInputAddCommentVenue.visibility = View.VISIBLE
            buttonSendCommentVenueDetails.visibility = View.VISIBLE
        }
    }

    override fun buttonListeners() {
        super.buttonListeners()
        binding.buttonSendCommentVenueDetails.setOnClickListener {
            val comment = binding.textInputAddCommentVenue.text.toString().trim()
            val isCommentFilled = validateInputFieldEmpty(binding.textInputLayoutWriteCommentVenueDetails, comment, getString(R.string.please_enter_comment))

            if (!isCommentFilled) {
                return@setOnClickListener
            }
            lifecycleScope.launch {
                if(
                    viewmodel.addComment(
                        AddCommentItem(
                            content = comment,
                            placeId = args.venueId
                        )
                    )
                ){
                    viewmodel.getComments(args.venueId)
                }
            }

            binding.textInputAddCommentVenue.text = null
        }

        binding.venueBackButton.setOnClickListener {
            findNavController().navigate(
                R.id.venuesFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.venuesFragment,false)
                    .build()
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(
                    R.id.venuesFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.venuesFragment,false)
                        .build()
                )
            }
        })
    }
    private fun setUI(venueDetailsItem: VenueDetailsItem) {
        with(binding) {
            textVenueName.text = venueDetailsItem.title
            textVenueDescription.text = venueDetailsItem.description
            textVenueType.text = venueDetailsItem.venueType
            textVenueOpenHours.text = venueDetailsItem.openHours
            textVenueLikeCount.text = venueDetailsItem.likeCount.toString()
            Glide.with(imageVenue)
                .load(venueDetailsItem.imageLinks[0])
                .placeholder(R.drawable.placeholder_venue)
                .error(R.drawable.placeholder_venue)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageVenue)


                //TODO -> like olsun request atsin, icon fill ol sun, like count text bir dene artsin

            buttonReadMoreVenues.post {
                val layout = textVenueDescription.layout
                val lines = layout.lineCount
                if (lines > 0) {
                    if (layout.getEllipsisCount(lines - 1) > 0) {
                        buttonReadMoreVenues.visibility = android.view.View.VISIBLE
                    } else {
                        buttonReadMoreVenues.visibility = android.view.View.GONE
                    }
                }
            }
            var flagRead = true
            buttonReadMoreVenues.setOnClickListener {
                if (flagRead) {
                    textVenueDescription.maxLines = java.lang.Integer.MAX_VALUE
                    buttonReadMoreVenues.text = "Read less"
                    flagRead = false
                } else {
                    buttonReadMoreVenues.text = "Read more"
                    textVenueDescription.maxLines = 3
                    flagRead = true
                }
            }
//            buttonReadMoreVenues.visibility = if (textVenueDescription.layout.lineCount < 3) View.GONE else View.VISIBLE
//            buttonReadMoreVenues.setOnClickListener {
//                if (textVenueDescription.layout.lineCount > 3 && textVenueDescription.maxLines < textVenueDescription.layout.lineCount) {
//                    textVenueDescription.maxLines += 3
//                }
//                if (buttonReadMoreVenues.text == "Read less") {
//                    textVenueDescription.maxLines = 3
//                    buttonReadMoreVenues.text = "Read more"
//                }
//                if (textVenueDescription.maxLines >= textVenueDescription.layout.lineCount) {
//                    buttonReadMoreVenues.text = "Read less"
//                }
//            }

            buttonVenueShowLocation.setOnClickListener {
                sharedViewModel.setCoordinates(
                    PlaceCoordinates(
                        placeId = venueDetailsItem.venueId,
                        name = venueDetailsItem.title,
                        placeType = "venue",
                        long = venueDetailsItem.coordinates.longitude,
                        lat = venueDetailsItem.coordinates.latitude
                    )
                )
                val bottomNavigationView =
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNavigationView.selectedItemId = R.id.mapFragment
            }
        }
    }

    private fun setAdapters() {
        binding.rvCommentsVenueDetails.adapter = commentAdapter
    }

    private fun updateAdapters(){
        lifecycleScope.launch {
            viewmodel.isLoadingComments.collectLatest {
                binding.progressBarCommentVenueDetails.isVisible = it
            }
        }
        lifecycleScope.launch {
            viewmodel.commentsState
                .filterNotNull()
                .collect {
                    binding.textNoCommentsTextVenueDetails.isVisible = it.isEmpty()
                    commentAdapter.updateAdapter(it)
                }
        }
    }

    private fun observer(){

        viewmodel.getComments(args.venueId)
        viewmodel.getVenueDetails(args.venueId)

        lifecycleScope.launch {
            viewmodel.venueDetails
                .filterNotNull()
                .collectLatest {
                    stopShimmer(binding.shimmerVenueDetails)
                    makeViewsVisible()
                    binding.venueBackButton.isVisible = true
                    setUI(it)
                }
        }

        lifecycleScope.launch {
            viewmodel.isLoadingComments.collectLatest {
                binding.progressBarCommentVenueDetails.isVisible = it
            }
        }
    }
}