package com.example.eventify.presentation.ui.fragments.venue

import androidx.activity.OnBackPressedCallback
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseFragment
import com.example.common.utils.crossfadeAppear
import com.example.common.utils.crossfadeDisappear
import com.example.common.utils.functions.dateFormatterIFYEAR_MNAMED_Comma_HM
import com.example.common.utils.functions.getInstantTime
import com.example.common.utils.functions.validateInputFieldEmpty
import com.example.common.utils.hideKeyboard
import com.example.common.utils.navigateWithAnimationFade
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

@AndroidEntryPoint
class VenueDetailsFragment : BaseFragment<FragmentVenueDetailsBinding>(FragmentVenueDetailsBinding::inflate) {

    private val viewmodel by viewModels<VenueDetailsViewModel>()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args by navArgs<VenueDetailsFragmentArgs>()

    private val commentAdapter = CommentAdapter()

    override fun onResume() {
        super.onResume()
        if (viewmodel.venueDetails.value == null)
            changeViewsVisibility(true)
    }

    override fun onViewCreatedLight() {
        binding.textVenueName.isSelected = true
        updateAdapters()
        setAdapters()
        observer()
    }

    override fun onPause() {
        super.onPause()
        changeViewsVisibility(false)
    }

    private fun changeViewsVisibility(makeInvisible: Boolean) {

        if (makeInvisible) {
            startShimmer(binding.shimmerVenueDetails)
            if (viewmodel.commentsState.value == null)
                binding.buttonSendCommentVenueDetails.isInvisible = true
        } else {
            stopShimmer(binding.shimmerVenueDetails)
            if (viewmodel.commentsState.value == null)
                crossfadeAppear(binding.progressBarCommentVenueDetails, 1000)
        }

        with(binding) {
            buttonBackVenue.isInvisible = makeInvisible
            textVenueName.isInvisible = makeInvisible
            imageVenue.isInvisible = makeInvisible
            textVenueDetailsText.isInvisible = makeInvisible
            textVenueDescription.isInvisible = makeInvisible
            textVenueTypeText.isInvisible = makeInvisible
            textVenueType.isInvisible = makeInvisible
            textVenueOpenHoursText.isInvisible = makeInvisible
            textVenueOpenHours.isInvisible = makeInvisible
            //textVenueLikesText.isInvisible = makeInvisible
            //textVenueLikeCount.isInvisible = makeInvisible
            buttonVenueShowLocation.isInvisible = makeInvisible
            textVenueDetailsCommentsText.isInvisible = makeInvisible
            textInputLayoutWriteCommentVenueDetails.isInvisible = makeInvisible
            textInputAddCommentVenue.isInvisible = makeInvisible
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

            viewmodel.addComment(
                AddCommentItem(
                    content = comment,
                    placeId = args.venueId
                )
            )

            viewmodel.userInfo?.let {
                commentAdapter.addComment(
                    CommentItem(
                        ownerId = -1,
                        commentId = -1,
                        username = it.username,
                        content = comment,
                        date = dateFormatterIFYEAR_MNAMED_Comma_HM(getInstantTime()),
                        isPending = true))
            }
            binding.textNoCommentsTextVenueDetails.isVisible = false
            binding.textInputAddCommentVenue.text = null
            binding.textInputLayoutWriteCommentVenueDetails.clearFocus()
            hideKeyboard(binding.root)
        }

        binding.buttonBackVenue.setOnClickListener {
            navigateWithAnimationFade(findNavController(), destination = R.id.venuesFragment, popUpTo = R.id.venueDetailsFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateWithAnimationFade(findNavController(), destination = R.id.venuesFragment, popUpTo = R.id.venueDetailsFragment)
                }
            })
    }

    private fun setUI(venueDetailsItem: VenueDetailsItem) {
        with(binding) {
            textVenueName.text = venueDetailsItem.title
            textVenueDescription.text = venueDetailsItem.description
            textVenueType.text = venueDetailsItem.venueType
            textVenueOpenHours.text = venueDetailsItem.openHours
            //textVenueLikeCount.text = String.format(venueDetailsItem.likeCount.toString())
            Glide.with(imageVenue)
                .load(venueDetailsItem.imageLink)
                .placeholder(R.drawable.placeholder_venue)
                .error(R.drawable.placeholder_venue)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageVenue)

            buttonReadMoreVenues.post {
                val layout = textVenueDescription.layout
                val lines = layout.lineCount
                if (lines > 0)
                    buttonReadMoreVenues.isVisible = layout.getEllipsisCount(lines - 1) > 0
            }
            var flagReadMore = true
            buttonReadMoreVenues.setOnClickListener {
                if (flagReadMore) {
                    buttonReadMoreVenues.text = getString(R.string.read_less)
                    textVenueDescription.maxLines = Integer.MAX_VALUE
                    flagReadMore = false
                } else {
                    buttonReadMoreVenues.text = getString(R.string.read_more)
                    textVenueDescription.maxLines = 5
                    flagReadMore = true
                }
            }


            buttonVenueShowLocation.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    sharedViewModel.setCoordinates(
                        LatLng(
                            venueDetailsItem.coordinates.latitude,
                            venueDetailsItem.coordinates.longitude
                        )
                    )
                }
                val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNavigationView.selectedItemId = R.id.mapFragment
            }
        }
    }

    private fun setAdapters() {
        binding.rvCommentsVenueDetails.adapter = commentAdapter
    }

    private fun updateAdapters() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.commentsState
                .filterNotNull()
                .collect {
                    binding.textNoCommentsTextVenueDetails.isVisible = it.isEmpty()
                    binding.rvCommentsVenueDetails.isVisible = it.isNotEmpty()
                    crossfadeDisappear(binding.progressBarCommentVenueDetails, 300, true)
                    crossfadeAppear(binding.buttonSendCommentVenueDetails, 500)
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
                    changeViewsVisibility(false)
                    setUI(it)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.isCommentAdded
                .collectLatest {
                    if(it){
                        commentAdapter.updateComment()
                        viewmodel.isCommentAdded.update { false }
                    }
                }
        }
    }
}