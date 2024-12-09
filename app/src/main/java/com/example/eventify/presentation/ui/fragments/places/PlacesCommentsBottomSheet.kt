package com.example.eventify.presentation.ui.fragments.places

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.eventify.common.base.BaseBottomSheetFragment
import com.example.eventify.databinding.BottomsheetPlacesCommentsBinding
import com.example.eventify.presentation.adapters.CommentAdapter
import com.example.eventify.presentation.viewmodels.VenueCommentsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlacesCommentsBottomSheet : BaseBottomSheetFragment<BottomsheetPlacesCommentsBinding>(BottomsheetPlacesCommentsBinding::inflate) {

    val cid = 12

    private val viewmodel by viewModels<VenueCommentsViewModel>()

    private val commentAdapter = CommentAdapter()

    override fun onViewCreatedLight() {
        viewmodel.getVenueName(cid)
        viewmodel.getComments(cid)
        observeVenueTitle()
        setAdapters()
        updateAdapters()
    }

    private fun observeVenueTitle() {
        lifecycleScope.launch {
            viewmodel.venueTitle.collectLatest { title ->
                if (title != null) {
                    binding.textPlaceNameComments.text = title
                }
            }
        }
    }

    private fun setAdapters() {
        binding.rvPlaceComments.adapter = commentAdapter
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewmodel.isLoading.collectLatest {
                binding.progressBarCommentBSH.isVisible = it
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