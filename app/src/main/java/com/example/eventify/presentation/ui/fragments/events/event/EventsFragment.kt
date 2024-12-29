package com.example.eventify.presentation.ui.fragments.events.event

import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.common.base.BaseFragment
import com.example.eventify.R
import com.example.common.utils.NancyToast
import com.example.data.remote.api.EventAPI
import com.example.domain.model.PlaceCoordinates
import com.example.eventify.databinding.FragmentEventsBinding
import com.example.eventify.presentation.adapters.EventAdapter
import com.example.eventify.presentation.viewmodels.SharedViewModel
import com.example.eventify.presentation.viewmodels.EventViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EventsFragment : BaseFragment<FragmentEventsBinding>(FragmentEventsBinding::inflate) {

    @Inject
    lateinit var api: EventAPI

    private val viewmodel: EventViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val eventAdapter = EventAdapter(
        onClickSeeComments = {
            val bundle = Bundle().apply {
                putInt("place_id", it.placeId)
                putString("place_title", it.name)
            }
            val bottomSheet = EventCommentsBottomSheet()
            bottomSheet.arguments = bundle
            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
        },
        onClickShowInMap = {
            sharedViewModel.setCoordinates(
                PlaceCoordinates(
                    placeId = it.placeId,
                    name = it.name,
                    placeType = "event",
                    long = it.lng,
                    lat = it.lat
                )
            )
            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNavigationView.selectedItemId = R.id.mapFragment
        },
        onClickBuyTicket = {
            NancyToast.makeText(requireContext(), "[buying ticket...]", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
            //TODO -> backendden mentiq
        }
    )

    override fun onViewCreatedLight() {
        setAdapters()
        updateAdapters()
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewmodel.isLoading.collectLatest {
                binding.progressIndicator.isVisible = it
            }
        }

        lifecycleScope.launch {
            viewmodel.events
                .filter { it.isNotEmpty() }
                .collect {
                eventAdapter.updateAdapter(it)
                Log.e("salam", it.toString())
            }
        }
    }

    private fun setAdapters() {
        binding.rvEvents.adapter = eventAdapter
    }
}