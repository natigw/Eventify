package com.example.eventify.presentation.ui.fragments.events.event

import android.view.View
import androidx.activity.OnBackPressedCallback
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
import com.example.common.utils.functions.dateFormatterIFYEAR_MNAMED_Comma_HM
import com.example.common.utils.functions.getInstantTime
import com.example.common.utils.hideKeyboard
import com.example.common.utils.functions.validateInputFieldEmpty
import com.example.common.utils.nancyToastSuccess
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
        if (viewmodel.eventDetails.value == null) {
            makeViewsInvisible()
            startShimmer(binding.shimmerEventDetails)
        }
    }

    override fun onViewCreatedLight() {
        binding.textEventName.isSelected = true
        setAdapters()
        updateAdapters()
        observer()
    }

    override fun onPause() {
        super.onPause()
        stopShimmer(binding.shimmerEventDetails)
        makeViewsVisible()
    }

    private fun makeViewsInvisible(){
        with(binding) {
            buttonBackEventDetails.visibility = View.INVISIBLE
            textEventName.visibility = View.INVISIBLE
            imageEvent.visibility = View.INVISIBLE
            buttonLikeEvent.visibility = View.INVISIBLE
            textEventDetailsText.visibility = View.INVISIBLE
            textEventDescription.visibility = View.INVISIBLE
            textEventTypeText.visibility = View.INVISIBLE
            textEventType.visibility = View.INVISIBLE
            textEventOrganizerText.visibility = View.INVISIBLE
            textEventOrganizer.visibility = View.INVISIBLE
            textEventDateText.visibility = View.INVISIBLE
            textEventDate.visibility = View.INVISIBLE
            textEventDurationHoursText.visibility = View.INVISIBLE
            textEventDurationHours.visibility = View.INVISIBLE
            textEventPublishedText.visibility = View.INVISIBLE
            textEventPublished.visibility = View.INVISIBLE
            textEventLikesText.visibility = View.INVISIBLE
            textEventLikeCount.visibility = View.INVISIBLE
            buttonEventShowLocation.visibility = View.INVISIBLE
            buttonBuyTicketEventDetails.visibility = View.INVISIBLE
            textEventDetailsCommentsText.visibility = View.INVISIBLE
            textInputLayoutWriteCommentEventDetails.visibility = View.INVISIBLE
            textInputEdittextAddCommentEvent.visibility = View.INVISIBLE
            buttonSendCommentEventDetails.visibility = View.INVISIBLE
        }
    }
    private fun makeViewsVisible(){
        with(binding) {
            buttonBackEventDetails.visibility = View.VISIBLE
            textEventName.visibility = View.VISIBLE
            imageEvent.visibility = View.VISIBLE
            textEventDetailsText.visibility = View.VISIBLE
            textEventDescription.visibility = View.VISIBLE
            textEventTypeText.visibility = View.VISIBLE
            textEventType.visibility = View.VISIBLE
            textEventOrganizerText.visibility = View.VISIBLE
            textEventOrganizer.visibility = View.VISIBLE
            textEventDateText.visibility = View.VISIBLE
            textEventDate.visibility = View.VISIBLE
            textEventDurationHoursText.visibility = View.VISIBLE
            textEventDurationHours.visibility = View.VISIBLE
            textEventPublishedText.visibility = View.VISIBLE
            textEventPublished.visibility = View.VISIBLE
            textEventLikesText.visibility = View.VISIBLE
            textEventLikeCount.visibility = View.VISIBLE
            buttonEventShowLocation.visibility = View.VISIBLE
            buttonBuyTicketEventDetails.visibility = View.VISIBLE
            textEventDetailsCommentsText.visibility = View.VISIBLE
            textInputLayoutWriteCommentEventDetails.visibility = View.VISIBLE
            textInputEdittextAddCommentEvent.visibility = View.VISIBLE
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
                .load(eventDetailsItem.imageLinks[0])
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
                nancyToastSuccess(requireContext(), getString(R.string.navigating_buy_ticket))
                //TODO -> backendden
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
                    stopShimmer(binding.shimmerEventDetails)
                    makeViewsVisible()
                    setUI(it)
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.isCommentAdded.collectLatest {
                if(it){
                    commentAdapter.updateComment()
                    viewmodel.isCommentAdded.update { false }
                }
                //TODO -> venue detailsde viewmodel.isCommentAdded.update { false } bu burdadi  -> bax gor hansi sehvdi
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
                    binding.progressBarCommentEventDetails.isVisible = false
                    crossfadeAppear(binding.buttonSendCommentEventDetails,500)
                    commentAdapter.updateAdapter(it)
                }
        }
    }
}