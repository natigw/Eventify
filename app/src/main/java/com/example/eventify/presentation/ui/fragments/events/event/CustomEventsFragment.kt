package com.example.eventify.presentation.ui.fragments.events.event

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.eventify.databinding.FragmentEventsBinding
import com.example.eventify.presentation.adapters.EventAdapter
import com.example.eventify.presentation.ui.fragments.events.PlacesFragmentDirections
import com.example.eventify.presentation.viewmodels.EventViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomEventsFragment : BaseFragment<FragmentEventsBinding>(FragmentEventsBinding::inflate) {

    private val viewmodel: EventViewModel by viewModels()

    private val eventAdapter = EventAdapter(
        onClick = {
            findNavController().navigate(PlacesFragmentDirections.actionPlacesFragmentToEventDetailsFragment(it.eventId))
        }
    )

    override fun onViewCreatedLight() {
        setAdapters()
        updateAdapters()
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewmodel.isLoading.collectLatest {
                binding.progressBarEvents.isVisible = it
            }
        }

        lifecycleScope.launch {
            viewmodel.events
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