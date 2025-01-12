package com.example.eventify.presentation.ui.fragments.events.event

import android.util.Log
import android.view.View
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
import com.example.common.utils.nancyToastSuccess
import com.example.common.utils.nancyToastWarning
import com.example.common.utils.startShimmer
import com.example.common.utils.stopShimmer
import com.example.domain.model.places.AddCommentItem
import com.example.domain.model.places.PlaceCoordinates
import com.example.domain.model.places.event.EventDetailsItem
import com.example.eventify.R
import com.example.eventify.databinding.FragmentEventDetailsBinding
import com.example.eventify.presentation.adapters.CommentAdapter
import com.example.eventify.presentation.viewmodels.EventDetailsViewModel
import com.example.eventify.presentation.viewmodels.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventDetailsFragment : BaseFragment<FragmentEventDetailsBinding>(FragmentEventDetailsBinding::inflate) {

    private val viewmodel by viewModels<EventDetailsViewModel>()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args by navArgs<EventDetailsFragmentArgs>() //id

    private val commentAdapter = CommentAdapter()

    override fun onResume() {
        super.onResume()
        if (viewmodel.isLoadingMain.value) {
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

    //yaxsi usul deyil hemise cagirilmaya biler
    override fun onPause() {
        super.onPause()
        stopShimmer(binding.shimmerEventDetails)
        makeViewsVisible()
    }

    private fun makeViewsInvisible(){
        with(binding) {
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
            textEventLikesText.visibility = View.INVISIBLE
        }
    }
    private fun makeViewsVisible(){
        with(binding) {
            textEventName.visibility = View.VISIBLE
            imageEvent.visibility = View.VISIBLE
            buttonLikeEvent.visibility = View.VISIBLE
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
            buttonSendCommentEventDetails.visibility = View.VISIBLE
            textEventLikesText.visibility = View.VISIBLE
        }
    }

    override fun buttonListener() {
        super.buttonListener()
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
            findNavController().navigate(
                R.id.placesFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.placesFragment,false)
                    .build()
            )
        }

        binding.buttonSendCommentEventDetails.setOnClickListener {
            if (binding.textInputEdittextAddCommentEvent.text.isNullOrEmpty()) {
                nancyToastWarning(requireContext(), getString(R.string.type_main_text_first))
                return@setOnClickListener

            }
            viewmodel.addComment(
                AddCommentItem(
                    content = binding.textInputEdittextAddCommentEvent.text.toString().trim(),
                    placeId = args.eventId
                )
            )
            binding.textInputEdittextAddCommentEvent.text = null
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
                .error(R.drawable.ic_launcher_foreground)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageEvent)

            buttonReadMoreEvents.post {
                val layout = textEventDescription.layout
                val lines = layout.lineCount
                if (lines > 0) {
                    if (layout.getEllipsisCount(lines - 1) > 0) {
                        buttonReadMoreEvents.visibility = View.VISIBLE
                    } else {
                        buttonReadMoreEvents.visibility = View.GONE
                    }
                }
            }
            var flagRead = true
            buttonReadMoreEvents.setOnClickListener {
                if (flagRead) {
                    textEventDescription.maxLines = Integer.MAX_VALUE
                    buttonReadMoreEvents.text = "Read less"
                    flagRead = false
                } else {
                    buttonReadMoreEvents.text = "Read more"
                    textEventDescription.maxLines = 3
                    flagRead = true
                }
            }

            buttonEventShowLocation.setOnClickListener {
                sharedViewModel.setCoordinates(
                    PlaceCoordinates(
                        placeId = eventDetailsItem.eventId,
                        name = eventDetailsItem.title,
                        placeType = "Event",
                        long = eventDetailsItem.coordinates.longitude,
                        lat = eventDetailsItem.coordinates.latitude
                    )
                )
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





        lifecycleScope.launch {
            viewmodel.isLoadingMain.collectLatest {
                stopShimmer(binding.shimmerEventDetails)
                makeViewsVisible()
            }
        }


        lifecycleScope.launch {
            viewmodel.likedState
                .filter { it!=null }
                .collectLatest {
                    Log.e("likedState",it.toString())
                    if(it!!){
                        binding.buttonLikeEvent.setIconResource(R.drawable.like_fav)
                    }
                    else{
                        binding.buttonLikeEvent.setIconResource(R.drawable.like_fav_border)
                    }
                    binding.buttonBackEventDetails.visibility = View.VISIBLE

                }
        }


        lifecycleScope.launch {
            viewmodel.eventDetails
                .filter { it != null }
                .collectLatest {
                    setUI(it!!)
                }
        }

        lifecycleScope.launch {
            viewmodel.isLoadingMain.collectLatest {
                binding.progressBarCommentEventDetails.isVisible = it
            }
        }

    }

    private fun setAdapters() {
        binding.rvCommentsEventDetails.adapter = commentAdapter
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewmodel.isLoadingComments.collectLatest {
                binding.progressBarCommentEventDetails.isVisible = it
            }
        }
        lifecycleScope.launch {
            viewmodel.comments
                .filter { it!=null }
                .collectLatest {
                    binding.textNoCommentsTextEventDetails.isVisible = it!!.isEmpty()
                    commentAdapter.updateAdapter(it)
                }
        }
    }
}