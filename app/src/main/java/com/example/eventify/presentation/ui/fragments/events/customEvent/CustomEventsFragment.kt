package com.example.eventify.presentation.ui.fragments.events.customEvent

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.eventify.R
import com.example.eventify.databinding.FragmentCustomEventsBinding
import com.example.eventify.presentation.adapters.EventAdapter
import com.example.eventify.presentation.ui.fragments.events.PlacesFragmentDirections
import com.example.eventify.presentation.viewmodels.EventViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomEventsFragment : BaseFragment<FragmentCustomEventsBinding>(FragmentCustomEventsBinding::inflate) {

    private val viewmodel: EventViewModel by viewModels()

    private val eventAdapter = EventAdapter(
        onLike = {
//            lifecycleScope.launch {
//                viewmodel.likeEvent(it)
//            }
        },
        onClick = {
            findNavController().navigate(PlacesFragmentDirections.actionPlacesFragmentToEventDetailsFragment(it))
        }
    )

    override fun onViewCreatedLight() {
        setAdapters()
        updateAdapters()
    }

    override fun buttonListener() {
        super.buttonListener()
        binding.buttonCreateCustomEvent.setOnClickListener {
            findNavController().navigate(PlacesFragmentDirections.actionPlacesFragmentToCreateCustomEventFragment())
        }
        binding.buttonCreateCustomEvent.setOnClickListener{
            findNavController().navigate(
                R.id.placesFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.placesFragment,false)
                    .build()
            )
        }
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewmodel.isLoading.collectLatest {
                binding.progressBarCustomEvents.isVisible = it
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
        binding.rvCustomEvents.adapter = eventAdapter
    }
}