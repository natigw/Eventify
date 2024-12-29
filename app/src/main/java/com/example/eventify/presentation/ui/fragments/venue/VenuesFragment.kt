package com.example.eventify.presentation.ui.fragments.venue

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
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
            findNavController().navigate(VenuesFragmentDirections.actionVenuesFragmentToVenueDetailsFragment(it.venueId))
        }
    )

    override fun onViewCreatedLight() {
        setAdapters()
        updateAdapters()
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewmodel.isLoading.collectLatest {
                binding.progressBarVenues.isVisible = it
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