package com.example.eventify.presentation.ui.fragments.map

import androidx.navigation.fragment.navArgs
import com.example.eventify.common.base.BaseBottomSheetFragment
import com.example.eventify.databinding.BottomsheetMarkerDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkerDetailsBottomSheet : BaseBottomSheetFragment<BottomsheetMarkerDetailsBinding>(BottomsheetMarkerDetailsBinding::inflate) {

    val args by navArgs<MarkerDetailsBottomSheetArgs>()

    override fun onViewCreatedLight() {
        binding.textVenueNameMarkerDetails.text = args.markerId.toString()
    }
}