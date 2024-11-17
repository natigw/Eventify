package com.example.eventify.presentation.ui.fragments.venues

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.common.utils.NancyToast
import com.example.eventify.data.remote.api.EventifyAPI
import com.example.eventify.databinding.FragmentVenuesBinding
import com.example.eventify.presentation.adapters.VenueAdapter
import com.example.eventify.presentation.viewmodels.VenueViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VenuesFragment : BaseFragment<FragmentVenuesBinding>(FragmentVenuesBinding::inflate) {

    @Inject
    lateinit var api: EventifyAPI

    private val viewmodel: VenueViewModel by viewModels()

    private val venueAdapter = VenueAdapter(
        onClick = {
            NancyToast.makeText(requireContext(), "[navigating...]", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
            //findNavController().navigate(R.id.)
        }
    )

    override fun onViewCreatedLight() {
        setUI()
        setAdapters()
        updateAdapters()
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewmodel.venues.collect {
                venueAdapter.updateAdapter(it)
            }
        }
    }

    private fun setAdapters() {
        binding.rvVenues.adapter = venueAdapter
    }

    private fun setUI() {

    }

    override fun observeChanges() {

    }
}