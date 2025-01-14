package com.example.eventify.presentation.ui.fragments.events.event

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.startShimmer
import com.example.common.utils.stopShimmer
import com.example.eventify.databinding.FragmentEventsBinding
import com.example.eventify.presentation.adapters.EventAdapter
import com.example.eventify.presentation.ui.fragments.events.PlacesFragmentDirections
import com.example.eventify.presentation.viewmodels.EventViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventsFragment : BaseFragment<FragmentEventsBinding>(FragmentEventsBinding::inflate) {

    private val viewModel: EventViewModel by viewModels()

    private val eventAdapter = EventAdapter(
        onLike = {
            viewModel.likeEvent(it)
        },
        onClick = {
            findNavController().navigate(
                PlacesFragmentDirections.actionPlacesFragmentToEventDetailsFragment(it, false)
            )
        }
    )

    override fun onResume() {
        super.onResume()
        if (viewModel.events.value.isEmpty())
            startShimmer(binding.shimmerEvents)
    }

    override fun onViewCreatedLight() {
        setAdapters()
        updateAdapters()
    }

    override fun onPause() {
        super.onPause()
        stopShimmer(binding.shimmerEvents)
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewModel.isLoading.collectLatest {
                stopShimmer(binding.shimmerEvents)
            }
        }
        lifecycleScope.launch {
            viewModel.events
                .filter { it.isNotEmpty() }
                .collect {
                    eventAdapter.updateAdapter(it)
                }
        }
    }

    private fun setAdapters() {
        binding.rvEvents.adapter = eventAdapter
    }
}