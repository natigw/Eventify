package com.example.eventify.presentation.ui.fragments.venue

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
import com.example.common.utils.NancyToast
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
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VenueDetailsFragment : BaseFragment<FragmentVenueDetailsBinding>(FragmentVenueDetailsBinding::inflate) {

    private val viewmodel by viewModels<VenueDetailsViewModel>()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val args by navArgs<VenueDetailsFragmentArgs>()

    private val commentAdapter = CommentAdapter()

    override fun onViewCreatedLight() {
        updateAdapters()
        setAdapters()
        observer()
    }


    override fun buttonListener() {
        super.buttonListener()
        binding.buttonSendCommentVenueDetails.setOnClickListener {
            if (binding.addCommentVenue.text.isNullOrEmpty()) {
                NancyToast.makeText(requireContext(),"Type main text first!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
                return@setOnClickListener
            }
            viewmodel.addComment(
                AddCommentItem(
                    content = binding.addCommentVenue.text.toString().trim(),
                    placeId = args.venueId
                )
            )
            binding.addCommentVenue.text = null
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
                .error(R.drawable.ic_launcher_foreground)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageVenue)

//            buttonLikeVenue.setOnClickListener {
//                if (buttonLikeVenue.tag == "not_liked") {
//                    buttonLikeVenue.setIconResource(R.drawable.like_fav)
//                    textVenueLikeCount.text = (venueDetailsItem.likeCount + 1).toString()
//                    buttonLikeVenue.tag = "liked"
//                } else {
//                    buttonLikeVenue.setIconResource(R.drawable.like_fav_border)
//                    textVenueLikeCount.text = venueDetailsItem.likeCount.toString()
//                    buttonLikeVenue.tag = "not_liked"
//                }
////                buttonLikeVenue.setIconResource(if (flag) R.drawable.like_fav else R.drawable.like_fav_border)
////                flag = !flag
////                buttonLikeVenue.setIconTintResource(R.color.purple_light_eventify)
////                textVenueLikeCount.text = if (flag) (venue.likeCount + 1).toString() else venue.likeCount.toString()
//                //TODO -> like olsun request atsin, icon fill olsun, like count text bir dene artsin
//            }

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

    private fun observer(){
        lifecycleScope.launch {
            viewmodel.getComments(args.venueId)
        }

        lifecycleScope.launch {
            viewmodel.getVenueDetails(args.venueId)
            viewmodel.isLoadingMain.collectLatest {
                binding.progressBarVenues.isVisible = it
            }
        }

        lifecycleScope.launch {
            viewmodel.venueDetails
                .filter { it != null }
                .collectLatest {
                    binding.venueBackButton.isVisible = true
                    setUI(it!!)
                }
        }

        lifecycleScope.launch {
            viewmodel.isLoadingMain.collectLatest {
                binding.progressBarCommentVenueDetails.isVisible = it
            }
        }



    }

    private fun updateAdapters(){
        lifecycleScope.launch {
            viewmodel.isLoadingComments.collectLatest {
                binding.progressBarCommentVenueDetails.isVisible = it
            }
        }
        lifecycleScope.launch {
            viewmodel.commentsState
                .filter { it!=null }
                .collect {
                    binding.textNoCommentsTextVenueDetails.isVisible = it!!.isEmpty()
                    commentAdapter.updateAdapter(it)
                }
        }
    }


    private fun setAdapters() {
        binding.rvCommentsVenueDetails.adapter = commentAdapter
    }

}