package com.example.eventify.presentation.ui.fragments.venues

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.common.utils.NancyToast
import com.example.eventify.data.remote.api.VenueAPI
import com.example.eventify.databinding.FragmentVenuesBinding
import com.example.eventify.domain.model.PlaceCoordinates
import com.example.eventify.presentation.adapters.VenueAdapter
import com.example.eventify.presentation.viewmodels.SharedViewModel
import com.example.eventify.presentation.viewmodels.VenueViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VenuesFragment : BaseFragment<FragmentVenuesBinding>(FragmentVenuesBinding::inflate) {

    @Inject
    lateinit var api: VenueAPI

    private val viewmodel: VenueViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val venueAdapter = VenueAdapter(
        onClick = {
            NancyToast.makeText(requireContext(), "[location data...]", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
//            findNavController().navigate(
//                VenuesFragmentDirections.actionVenuesFragmentToTestMapFragment(
//                    PlaceCoordinates(
//                        it.name,
//                        it.lngCoordinate,
//                        it.latCoordinate)
//                )
//            )
            sharedViewModel.setCoordinates(
                PlaceCoordinates(
                        it.name,
                        it.lngCoordinate,
                        it.latCoordinate
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
            viewmodel.venues.collect {
                venueAdapter.updateAdapter(it)
                Log.e("salam", it.toString())
            }
        }
    }

    private fun setAdapters() {
        binding.rvVenues.adapter = venueAdapter
    }
}