package com.example.eventify.presentation.ui.fragments.venue

import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.example.common.utils.functions.dateFormatterIFYEAR_MNAMED_Comma_HM
import com.example.common.utils.functions.getInstantTime
import com.example.common.utils.functions.validateInputFieldEmpty
import com.example.common.utils.navigateWithAnimationLeftToRight
import com.example.common.utils.startShimmer
import com.example.common.utils.stopShimmer
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.domain.model.places.venue.VenueDetailsItem
import com.example.eventify.R
import com.example.eventify.databinding.FragmentVenueDetailsBinding
import com.example.eventify.presentation.adapters.CommentAdapter
import com.example.eventify.presentation.viewmodels.SharedViewModel
import com.example.eventify.presentation.viewmodels.VenueDetailsViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant

@AndroidEntryPoint
class VenueDetailsFragment :
    BaseFragment<FragmentVenueDetailsBinding>(FragmentVenueDetailsBinding::inflate) {

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

    private fun makeViewsInvisible() {
        with(binding) {
            buttonBackVenue.visibility = View.INVISIBLE
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

    private fun makeViewsVisible() {
        with(binding) {
            buttonBackVenue.visibility = View.VISIBLE
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
            val isCommentFilled = validateInputFieldEmpty(
                binding.textInputLayoutWriteCommentVenueDetails,
                comment,
                getString(R.string.please_enter_comment)
            )

            if (!isCommentFilled) {
                return@setOnClickListener
            }



            viewmodel.addComment(
                AddCommentItem(
                    content = comment,
                    placeId = args.venueId
                )
            )
            binding.textNoCommentsTextVenueDetails.isVisible = false

            viewmodel.userInfo?.let {
                commentAdapter.addComment(
                    CommentItem(
                        -1,
                        -1,
                        it.username,
                        comment,
                        dateFormatterIFYEAR_MNAMED_Comma_HM(getInstantTime()),
                        true))
            }

            binding.textInputAddCommentVenue.text = null
        }

        binding.buttonBackVenue.setOnClickListener {
            findNavController().navigate(
                R.id.venuesFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.venueDetailsFragment,true)
                    .build()
                )


        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(
                        R.id.venuesFragment,
                        null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.venueDetailsFragment,true)
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
                    textVenueDescription.maxLines = Integer.MAX_VALUE
                    buttonReadMoreVenues.text = getString(R.string.read_less)
                    flagRead = false
                } else {
                    buttonReadMoreVenues.text = getString(R.string.read_more)
                    textVenueDescription.maxLines = 3
                    flagRead = true
                }
            }

            buttonVenueShowLocation.setOnClickListener {
                venueDetailsItem.coordinates.latitude
                viewLifecycleOwner.lifecycleScope.launch {
                    sharedViewModel.setCoordinates(
                        LatLng(
                            venueDetailsItem.coordinates.latitude,
                            venueDetailsItem.coordinates.longitude
                        )
                    )

                }
                val bottomNavigationView =
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNavigationView.selectedItemId = R.id.mapFragment
            }
        }
    }

    private fun setAdapters() {
        binding.rvCommentsVenueDetails.adapter = commentAdapter
    }

    private fun updateAdapters() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.isLoadingComments.collectLatest {
                binding.progressBarCommentVenueDetails.isVisible = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.commentsState
                .filterNotNull()
                .collect {
                    binding.textNoCommentsTextVenueDetails.isVisible = it.isEmpty()
                    commentAdapter.updateAdapter(it)
                }
        }
    }

    private fun observer() {


        viewmodel.getComments(args.venueId)
        viewmodel.getVenueDetails(args.venueId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.venueDetails
                .filterNotNull()
                .collectLatest {
                    stopShimmer(binding.shimmerVenueDetails)
                    makeViewsVisible()
                    setUI(it)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.isLoadingComments.collectLatest {
                binding.progressBarCommentVenueDetails.isVisible = it
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.isCommentAdded
                .collectLatest {
                    if(it){
                        commentAdapter.updateComment()
                    }
                    viewmodel.isCommentAdded.update { false }
                }
        }

    }
}