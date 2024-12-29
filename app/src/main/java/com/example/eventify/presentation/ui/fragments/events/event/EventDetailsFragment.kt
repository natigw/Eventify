package com.example.eventify.presentation.ui.fragments.events.event

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseFragment
import com.example.common.utils.NancyToast
import com.example.domain.model.AddCommentItem
import com.example.domain.model.PlaceCoordinates
import com.example.domain.model.EventDetailsItem
import com.example.eventify.R
import com.example.eventify.databinding.FragmentEventDetailsBinding
import com.example.eventify.presentation.adapters.CommentAdapter
import com.example.eventify.presentation.viewmodels.SharedViewModel
import com.example.eventify.presentation.viewmodels.EventDetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventDetailsFragment : BaseFragment<FragmentEventDetailsBinding>(FragmentEventDetailsBinding::inflate) {

    private val viewmodel by viewModels<EventDetailsViewModel>()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args by navArgs<EventDetailsFragmentArgs>()

    private val commentAdapter = CommentAdapter()

    override fun onViewCreatedLight() {

        lifecycleScope.launch {
            viewmodel.getEventDetails(args.eventId)
            viewmodel.isLoadingMain.collectLatest {
                binding.progressBarEvents.isVisible = it
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

        binding.buttonSendCommentEventDetails.setOnClickListener {
            if (binding.addCommentEvent.text.isNullOrEmpty()) {
                NancyToast.makeText(requireContext(),"Type main text first!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
                return@setOnClickListener
            }
            viewmodel.addComment(
                AddCommentItem(
                    content = binding.addCommentEvent.text.toString().trim(),
                    placeId = args.eventId
                )
            )
            binding.addCommentEvent.text = null
        }

        viewmodel.getComments(args.eventId)
        setAdapters()
        updateAdapters()
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

            var flag = true
            buttonLikeEvent.setOnClickListener {
                if (buttonLikeEvent.tag == "not_liked") {
                    buttonLikeEvent.setIconResource(R.drawable.like_fav)
                    textEventLikeCount.text = (eventDetailsItem.likeCount + 1).toString()
                    buttonLikeEvent.tag = "liked"
                } else {
                    buttonLikeEvent.setIconResource(R.drawable.like_fav_border)
                    textEventLikeCount.text = eventDetailsItem.likeCount.toString()
                    buttonLikeEvent.tag = "not_liked"
                }
//                buttonLikeEvent.setIconResource(if (flag) R.drawable.like_fav else R.drawable.like_fav_border)
//                flag = !flag
//                buttonLikeEvent.setIconTintResource(R.color.purple_light_eventify)
//                textEventLikeCount.text = if (flag) (Event.likeCount + 1).toString() else Event.likeCount.toString()
                //TODO -> like olsun request atsin, icon fill olsun, like count text bir dene artsin
            }

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
                NancyToast.makeText(requireContext(), "[buying ticket...]", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
                //TODO -> backendden
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
                binding.textNoCommentsTextEventDetails.isInvisible = it
            }
        }
        lifecycleScope.launch {
            viewmodel.comments
                .filter { it.isNotEmpty() }
                .collect {
                    commentAdapter.updateAdapter(it)
                }
        }
    }
}