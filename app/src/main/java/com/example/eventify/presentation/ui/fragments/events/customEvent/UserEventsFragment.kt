package com.example.eventify.presentation.ui.fragments.events.customEvent

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.startShimmer
import com.example.common.utils.stopShimmer
import com.example.eventify.databinding.FragmentUserEventsBinding
import com.example.eventify.presentation.adapters.EventAdapter
import com.example.eventify.presentation.ui.fragments.events.PlacesFragmentDirections
import com.example.eventify.presentation.viewmodels.UserEventsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserEventsFragment : BaseFragment<FragmentUserEventsBinding>(FragmentUserEventsBinding::inflate) {

    private val viewmodel by viewModels<UserEventsViewModel>()

    private val eventAdapter = EventAdapter(
        onLike = {
            //TODO -> backend user eventleri ayirandan sora ayrica like qos
//            viewLifecycleOwner.lifecycleScope.launch {
//                viewmodel.likeEvent(it)
//            }
        },
        onClick = {
            findNavController().navigate(PlacesFragmentDirections.actionPlacesFragmentToEventDetailsFragment(it, false))
        }
    )

    override fun onResume() {
        super.onResume()
        if (viewmodel.userEvents.value == null)
            startShimmer(binding.shimmerUserEvents)
    }

    override fun onViewCreatedLight() {
        setAdapters()
        updateAdapters()
    }

    override fun onPause() {
        super.onPause()
        stopShimmer(binding.shimmerUserEvents)
    }

    override fun buttonListeners() {
        super.buttonListeners()
        binding.buttonCreateCustomEvent.setOnClickListener {
            findNavController().navigate(PlacesFragmentDirections.actionPlacesFragmentToCreateCustomEventFragment())
        }
    }

    private fun setAdapters() {
        binding.rvUserEvents.adapter = eventAdapter
    }

    private fun updateAdapters() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.userEvents
                .filterNotNull()
                .collectLatest {
                    binding.textNoUserEventsTEXT.isVisible = it.isEmpty()
                    stopShimmer(binding.shimmerUserEvents)
                    eventAdapter.updateAdapter(it)
                }
        }
    }
}