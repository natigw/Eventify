package com.example.eventify.presentation.ui.fragments.places.event

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.common.base.BaseBottomSheetFragment
import com.example.common.utils.NancyToast
import com.example.domain.model.AddCommentItem
import com.example.eventify.databinding.BottomsheetPlacesCommentsBinding
import com.example.eventify.presentation.adapters.CommentAdapter
import com.example.eventify.presentation.viewmodels.EventCommentsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventCommentsBottomSheet : BaseBottomSheetFragment<BottomsheetPlacesCommentsBinding>(BottomsheetPlacesCommentsBinding::inflate) {
    private val viewmodel by viewModels<EventCommentsViewModel>()

    private val commentAdapter = CommentAdapter()

    override fun onViewCreatedLight() {
        val args = arguments
        val placeId = args?.getInt("place_id")!!
        val placeTitle = args.getString("place_title")
        binding.textPlaceNameComments.text = placeTitle
        viewmodel.getEventName(placeId)
        viewmodel.getComments(placeId)

//        observeEventTitle()
        setAdapters()
        updateAdapters()

        binding.buttonSendComment.setOnClickListener {
            if (binding.addComment.text.isNullOrEmpty()) {
                NancyToast.makeText(requireContext(), "Type main text first!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
                return@setOnClickListener
            }
            viewmodel.addComment(
                AddCommentItem(
                content = binding.addComment.text.toString().trim(),
                placeId = placeId
            ))
            binding.addComment.text = null
        }
    }

//    private fun observeEventTitle() {
//        lifecycleScope.launch {
//            viewmodel.eventTitle.collectLatest { title ->
//                if (title != null) {
//                    binding.textPlaceNameComments.text = title
//                }
//            }
//        }
//    }

    private fun setAdapters() {
        binding.rvPlaceComments.adapter = commentAdapter
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewmodel.isLoading.collectLatest {
                binding.progressBarCommentBSH.isVisible = it
                binding.noCommentsText.isInvisible = it
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