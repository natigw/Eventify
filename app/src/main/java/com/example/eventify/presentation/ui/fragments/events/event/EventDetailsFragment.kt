package com.example.eventify.presentation.ui.fragments.events.event

import android.content.Intent
import android.net.Uri
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
import com.example.common.utils.nancyToastWarning
import com.example.common.utils.navigateWithAnimationFade
import com.example.common.utils.startShimmer
import com.example.common.utils.stopShimmer
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.CommentItem
import com.example.domain.model.places.event.EventDetailsItem
import com.example.eventify.R
import com.example.eventify.databinding.FragmentEventDetailsBinding
import com.example.eventify.presentation.adapters.CommentAdapter
import com.example.eventify.presentation.viewmodels.EventDetailsViewModel
import com.example.eventify.presentation.viewmodels.SharedViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventDetailsFragment : BaseFragment<FragmentEventDetailsBinding>(FragmentEventDetailsBinding::inflate) {

    private val viewmodel by viewModels<EventDetailsViewModel>()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args by navArgs<EventDetailsFragmentArgs>() //id, where it comes

    private val commentAdapter = CommentAdapter()

    override fun onResume() {
        super.onResume()
        if (viewmodel.eventDetails.value == null)
            changeViewsVisibility(true)
    }

    override fun onViewCreatedLight() {
        binding.textEventName.isSelected = true
        setAdapters()
        updateAdapters()
        observer()
    }

    override fun onPause() {
        super.onPause()
        changeViewsVisibility(false)
    }

    private fun changeViewsVisibility(makeInvisible: Boolean) {

        if (makeInvisible) {
            startShimmer(binding.shimmerEventDetails)
            if (viewmodel.likedState.value == null)
                binding.buttonLikeEvent.isInvisible = true
            if (viewmodel.comments.value == null)
                binding.buttonSendCommentEventDetails.isInvisible = true
        } else {
            stopShimmer(binding.shimmerEventDetails)
            if (viewmodel.comments.value == null)
                crossfadeAppear(binding.progressBarCommentEventDetails, 1000)
        }

        with(binding) {
            buttonBackEventDetails.isInvisible = makeInvisible
            textEventName.isInvisible = makeInvisible
            imageEvent.isInvisible = makeInvisible
            textEventDetailsText.isInvisible = makeInvisible
            textEventDescription.isInvisible = makeInvisible
            textEventTypeText.isInvisible = makeInvisible
            textEventType.isInvisible = makeInvisible
            textEventOrganizerText.isInvisible = makeInvisible
            textEventOrganizer.isInvisible = makeInvisible
            textEventDateText.isInvisible = makeInvisible
            textEventDate.isInvisible = makeInvisible
            textEventDurationHoursText.isInvisible = makeInvisible
            textEventDurationHours.isInvisible = makeInvisible
            textEventPublishedText.isInvisible = makeInvisible
            textEventPublished.isInvisible = makeInvisible
            textEventLikesText.isInvisible = makeInvisible
            textEventLikeCount.isInvisible = makeInvisible
            buttonEventShowLocation.isInvisible = makeInvisible
            buttonBuyTicketEventDetails.isInvisible = makeInvisible
            textEventDetailsCommentsText.isInvisible = makeInvisible
            textInputLayoutWriteCommentEventDetails.isInvisible = makeInvisible
            textInputEdittextAddCommentEvent.isInvisible = makeInvisible
        }
    }

    override fun buttonListeners() {
        super.buttonListeners()
        binding.buttonLikeEvent.setOnClickListener{
            if(viewmodel.likedState.value == true){
                viewmodel.likedState.update { false }
            }
            else{
                viewmodel.likedState.update { true }
            }
            viewmodel.updateLikeEvent(args.eventId)
        }

        binding.buttonBackEventDetails.setOnClickListener {
            if(args.comingProfile)
                navigateWithAnimationFade(findNavController(), R.id.profileFragment, R.id.eventDetailsFragment)
            else
                navigateWithAnimationFade(findNavController(), R.id.placesFragment, R.id.eventDetailsFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(args.comingProfile)
                    navigateWithAnimationFade(findNavController(), R.id.profileFragment, R.id.eventDetailsFragment)
                else
                    navigateWithAnimationFade(findNavController(), R.id.placesFragment, R.id.eventDetailsFragment)
            }
        })


        binding.buttonSendCommentEventDetails.setOnClickListener {
            val comment = binding.textInputEdittextAddCommentEvent.text.toString().trim()
            val isCommentFilled = validateInputFieldEmpty(binding.textInputLayoutWriteCommentEventDetails, comment, getString(R.string.please_enter_comment))

            if (!isCommentFilled) {
                return@setOnClickListener
            }

            viewmodel.addComment(
                AddCommentItem(
                    content = comment,
                    placeId = args.eventId
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
                        isPending = true
                    )
                )
            }
            binding.textNoCommentsTextEventDetails.isVisible = false
            binding.textInputEdittextAddCommentEvent.text = null
            binding.textInputLayoutWriteCommentEventDetails.editText?.setText("")
            binding.textInputLayoutWriteCommentEventDetails.clearFocus()
            hideKeyboard(binding.root)
        }
    }

    private fun setUI(eventDetailsItem: EventDetailsItem) {
        with(binding) {
            textEventName.text = eventDetailsItem.title
            textEventDescription.text = eventDetailsItem.description
            textEventType.text = eventDetailsItem.eventType
            textEventOrganizer.text = eventDetailsItem.organizer
            textEventDate.text = eventDetailsItem.eventDate
            textEventPublished.text = eventDetailsItem.publishingDate
            textEventDurationHours.text = eventDetailsItem.eventDurationHours
            textEventLikeCount.text = String.format(eventDetailsItem.likeCount.toString())
            Glide.with(imageEvent)
                .load(eventDetailsItem.imageLink)
                .placeholder(R.drawable.placeholder_event)
                .error(R.drawable.placeholder_event)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageEvent)

            buttonReadMoreEvents.post {
                val layout = textEventDescription.layout
                val lines = layout.lineCount
                if (lines > 0)
                    buttonReadMoreEvents.isVisible = layout.getEllipsisCount(lines - 1) > 0
            }
            var flagRead = true
            buttonReadMoreEvents.setOnClickListener {
                if (flagRead) {
                    textEventDescription.maxLines = Integer.MAX_VALUE
                    buttonReadMoreEvents.text = getString(R.string.read_less)
                    flagRead = false
                } else {
                    buttonReadMoreEvents.text = getString(R.string.read_more)
                    textEventDescription.maxLines = 5
                    flagRead = true
                }
            }

            buttonEventShowLocation.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    sharedViewModel.setCoordinates(
                        LatLng(
                            eventDetailsItem.coordinates.latitude,
                            eventDetailsItem.coordinates.longitude
                        )
                    )
                }
                val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNavigationView.selectedItemId = R.id.mapFragment
            }

            buttonBuyTicketEventDetails.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewmodel.eventDetails
                        .filterNotNull()
                        .collectLatest {
                            try {
                                val response = viewmodel.getSearchDetails(it.title)
                                val url = "https://iticket.az/events/${response.category}/${response.slug}"
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse(url)
                                }
                                startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                nancyToastWarning(requireContext(), getString(R.string.unable_to_open_link))
                            }
                    }
                }
            }
        }
    }


    private fun observer(){
        viewmodel.getComments(args.eventId)
        viewmodel.getEventDetails(args.eventId)
        viewmodel.getEventLikeInfo(args.eventId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.likedState
                .filterNotNull()
                .collectLatest {
                    if(it)
                        binding.buttonLikeEvent.setIconResource(R.drawable.like_fav)
                    else
                        binding.buttonLikeEvent.setIconResource(R.drawable.like_fav_border)
                    crossfadeAppear(binding.buttonLikeEvent)
                }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.eventDetails
                .filterNotNull()
                .collectLatest {
                    changeViewsVisibility(false)
                    setUI(it)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.isCommentAdded.collectLatest {
                if(it){
                    commentAdapter.updateComment()
                    viewmodel.isCommentAdded.update { false }
                }
            }
        }
    }

    private fun setAdapters() {
        binding.rvCommentsEventDetails.adapter = commentAdapter
    }

    private fun updateAdapters() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.comments
                .filterNotNull()
                .collectLatest {
                    binding.textNoCommentsTextEventDetails.isVisible = it.isEmpty()
                    binding.rvCommentsEventDetails.isVisible = it.isNotEmpty()
                    crossfadeDisappear(binding.progressBarCommentEventDetails, 300, true)
                    crossfadeAppear(binding.buttonSendCommentEventDetails,500)
                    commentAdapter.updateAdapter(it)
                }
        }
    }
}