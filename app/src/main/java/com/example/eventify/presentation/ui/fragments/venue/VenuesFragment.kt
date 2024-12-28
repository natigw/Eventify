package com.example.eventify.presentation.ui.fragments.venue

import android.util.Log
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

//@AndroidEntryPoint
//class VenuesFragment : BaseFragment<FragmentVenuesBinding>(FragmentVenuesBinding::inflate) {
//
//    private val viewmodel: VenueViewModel by viewModels()
//    private val sharedViewModel: SharedViewModel by activityViewModels()
//
//    private val venueAdapter = VenueAdapter(
//        onClickSeeComments = {
//            val bundle = Bundle().apply {
//                putInt("place_id", it.placeId)
//                putString("place_title", it.name)
//            }
//            val bottomSheet = VenueCommentsBottomSheet()
//            bottomSheet.arguments = bundle
//            bottomSheet.show(parentFragmentManager, bottomSheet.tag)
//        },
//        onClickShowInMap = {
//            sharedViewModel.setCoordinates(
//                PlaceCoordinates(
//                    placeId = it.placeId,
//                    name = it.name,
//                    placeType = "venue",
//                    long = it.lngCoordinate,
//                    lat = it.latCoordinate
//                )
//            )
//            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
//            bottomNavigationView.selectedItemId = R.id.mapFragment
//        }
//    )
//
//    override fun onViewCreatedLight() {
//        setAdapters()
//        updateAdapters()
//    }
//
//    private fun updateAdapters() {
//        lifecycleScope.launch {
//            viewmodel.isLoading.collectLatest {
//                binding.progressIndicator.isVisible = it
//            }
//        }
//
//        lifecycleScope.launch {
//            viewmodel.venues
//                .filter { it.isNotEmpty() }
//                .collect {
//                venueAdapter.updateAdapter(it)
//                Log.e("salam", it.toString())
//            }
//        }
//    }
//
//    private fun setAdapters() {
//        binding.rvVenues.adapter = venueAdapter
//    }
//}




@AndroidEntryPoint
class VenuesFragment : BaseFragment<FragmentVenuesBinding>(FragmentVenuesBinding::inflate) {

    private val viewmodel: VenueViewModel by viewModels()

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