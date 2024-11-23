package com.example.eventify.presentation.ui.fragments.places

import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.databinding.FragmentPlacesBinding

class PlacesFragment : BaseFragment<FragmentPlacesBinding>(FragmentPlacesBinding::inflate) {
    override fun onViewCreatedLight() {

        binding.button5.setOnClickListener {
            findNavController().navigate(R.id.venuesFragment)
        }
        binding.button6.setOnClickListener {
            findNavController().navigate(R.id.eventsFragment)
        }

    }
}