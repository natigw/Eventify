package com.example.eventify.presentation.ui.fragments.events.customEvent

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.startShimmer
import com.example.common.utils.stopShimmer
import com.example.eventify.R
import com.example.eventify.databinding.FragmentCustomEventsBinding
import com.example.eventify.presentation.adapters.EventAdapter
import com.example.eventify.presentation.ui.fragments.events.PlacesFragmentDirections
import com.example.eventify.presentation.viewmodels.EventViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CustomEventsFragment : BaseFragment<FragmentCustomEventsBinding>(FragmentCustomEventsBinding::inflate) {

    private val viewmodel by viewModels<EventViewModel>()

    private val eventAdapter = EventAdapter(
        onLike = {
//            lifecycleScope.launch {
//                viewmodel.likeEvent(it)
//            }
        },
        onClick = {
            findNavController().navigate(PlacesFragmentDirections.actionPlacesFragmentToEventDetailsFragment(it, false))
        }
    )

    override fun onResume() {
        super.onResume()
        if (viewmodel.events.value == null)
            startShimmer(binding.shimmerCustomEvents)
    }

    override fun onViewCreatedLight() {
        setAdapters()
        updateAdapters()
    }

    override fun onPause() {
        super.onPause()
        stopShimmer(binding.shimmerCustomEvents)
    }

    override fun buttonListeners() {
        super.buttonListeners()
//        binding.buttonCreateCustomEvent.setOnClickListener {
//            findNavController().navigate(PlacesFragmentDirections.actionPlacesFragmentToCreateCustomEventFragment())
//        }
        binding.buttonCreateCustomEvent.setOnClickListener{
            findNavController().navigate(
                R.id.createCustomEventFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.placesFragment,false)
                    .build()
            )
        }
    }

    private fun setAdapters() {
        binding.rvCustomEvents.adapter = eventAdapter
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewmodel.events
                .filterNotNull()
                .collectLatest {
                    binding.textNoCustomEventsTEXT.isVisible = it.isEmpty()
                    stopShimmer(binding.shimmerCustomEvents)
                    eventAdapter.updateAdapter(it)
                }
        }
    }
}