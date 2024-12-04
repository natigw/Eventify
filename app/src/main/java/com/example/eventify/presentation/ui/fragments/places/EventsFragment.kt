package com.example.eventify.presentation.ui.fragments.places

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.common.utils.NancyToast
import com.example.eventify.data.remote.api.EventAPI
import com.example.eventify.databinding.FragmentEventsBinding
import com.example.eventify.domain.model.PlaceCoordinates
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
        onClick = {
            NancyToast.makeText(requireContext(), "[location data...]", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
//            findNavController().navigate(
//                EventsFragmentDirections.actionEventsFragmentToTestMapFragment(
//                    PlaceCoordinates(
//                        it.name,
//                        it.lngCoordinate,
//                        it.latCoordinate)
//                )
//            )
            sharedViewModel.setCoordinates(
                PlaceCoordinates(
                    id = it.id,
                    name = it.name,
                    placeType = "event",
                    long = it.lngCoordinate,
                    lat = it.latCoordinate
                ))
            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNavigationView.selectedItemId = R.id.testMapFragment
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