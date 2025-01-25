package com.example.eventify.presentation.ui.fragments.events.event

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.startShimmer
import com.example.common.utils.stopShimmer
import com.example.eventify.R
import com.example.eventify.databinding.FragmentEventsBinding
import com.example.eventify.presentation.adapters.EventAdapter
import com.example.eventify.presentation.ui.fragments.events.PlacesFragmentDirections
import com.example.eventify.presentation.viewmodels.EventViewModel
import com.example.eventify.presentation.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventsFragment : BaseFragment<FragmentEventsBinding>(FragmentEventsBinding::inflate) {

    private val viewModel by viewModels<EventViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()


    private val eventAdapter = EventAdapter(
        onLike = {
            viewModel.likeEvent(it)
        },
        onClick = {
            findNavController().navigate(
                PlacesFragmentDirections.actionPlacesFragmentToEventDetailsFragment(it, false)
            )
            sharedViewModel.eventsRVState = binding.rvEvents.layoutManager?.onSaveInstanceState()
        }

    )

    override fun onResume() {
        super.onResume()
        if (viewModel.events.value == null)
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

    private fun setAdapters() {
        binding.rvEvents.adapter = eventAdapter
    }

    private fun updateAdapters() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events
                .filterNotNull()
                .collectLatest {
                    binding.textNoEventsTEXT.isVisible = it.isEmpty()
                    stopShimmer(binding.shimmerEvents)
                    eventAdapter.updateAdapter(it)
                    sharedViewModel.eventsRVState?.let { state->
                        binding.rvEvents.layoutManager?.onRestoreInstanceState(state)
                    }
                }
        }
    }
}