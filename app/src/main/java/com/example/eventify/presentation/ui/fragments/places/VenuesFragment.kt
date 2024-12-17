package com.example.eventify.presentation.ui.fragments.places

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.common.base.BaseFragment
import com.example.eventify.databinding.FragmentVenuesBinding
import com.example.domain.model.PlaceCoordinates
import com.example.eventify.presentation.adapters.VenueAdapter
import com.example.eventify.presentation.viewmodels.SharedViewModel
import com.example.eventify.presentation.viewmodels.VenueViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VenuesFragment : BaseFragment<FragmentVenuesBinding>(FragmentVenuesBinding::inflate) {

    private val viewmodel: VenueViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val venueAdapter = VenueAdapter(
        onClickSeeComments = {
            findNavController().navigate(VenuesFragmentDirections.actionVenuesFragmentToMapFragment())
        },
        onClickShowInMap = {
            sharedViewModel.setCoordinates(
                com.example.domain.model.PlaceCoordinates(
                    placeId = it.placeId,
                    name = it.name,
                    placeType = "venue",
                    long = it.lngCoordinate,
                    lat = it.latCoordinate
                )
            )
            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNavigationView.selectedItemId = R.id.mapFragment
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
            viewmodel.venues
                .filter { it.isNotEmpty() }
                .collect {
                venueAdapter.updateAdapter(it)
                Log.e("salam", it.toString())
            }
        }
    }

    private fun setAdapters() {
        binding.rvVenues.adapter = venueAdapter
    }
}