package com.example.eventify.presentation.ui.fragments.events.event

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
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
class EventsFragment : BaseFragment<FragmentEventsBinding>(FragmentEventsBinding::inflate) {

    private val viewModel: EventViewModel by viewModels()

    private val eventAdapter = EventAdapter(
        onLike = {
            lifecycleScope.launch {
                viewModel.likeEvent(it)
            }
        },
        onClick = {
            findNavController().navigate(PlacesFragmentDirections.actionPlacesFragmentToEventDetailsFragment(it))
        }
    )

    override fun onViewCreatedLight() {
        setAdapters()
        updateAdapters()






    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewModel.isLoading.collectLatest {
                binding.progressBarEvents.isVisible = it
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