package com.example.eventify.presentation.ui.fragments.venue

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.startShimmer
import com.example.common.utils.stopShimmer
import com.example.eventify.databinding.FragmentVenuesBinding
import com.example.eventify.presentation.adapters.VenueAdapter
import com.example.eventify.presentation.viewmodels.VenueViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VenuesFragment : BaseFragment<FragmentVenuesBinding>(FragmentVenuesBinding::inflate) {

    private val viewmodel by viewModels<VenueViewModel>()

    private val venueAdapter = VenueAdapter(
        onClick = {
            findNavController().navigate(
                VenuesFragmentDirections.actionVenuesFragmentToVenueDetailsFragment(it.venueId)
            )
        }
    )

    override fun onResume() {
        super.onResume()
        if (viewmodel.venues.value.isEmpty())
            startShimmer(binding.shimmerVenues)
    }

    override fun onViewCreatedLight() {
        setAdapters()
        updateAdapters()
    }

    override fun onPause() {
        super.onPause()
        stopShimmer(binding.shimmerVenues)
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewmodel.isLoading.collectLatest {
                stopShimmer(binding.shimmerVenues)
            }
        }

        lifecycleScope.launch {
            viewmodel.venues
                .filter { it.isNotEmpty() }
                .collect {
                    venueAdapter.updateAdapter(it)
                }
        }
    }

    private fun setAdapters() {
        binding.rvVenues.adapter = venueAdapter
    }
}