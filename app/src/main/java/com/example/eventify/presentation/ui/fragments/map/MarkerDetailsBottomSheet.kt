package com.example.eventify.presentation.ui.fragments.map

import androidx.navigation.fragment.navArgs
import com.example.eventify.common.base.BaseBottomSheetFragment
import com.example.eventify.common.utils.NancyToast
import com.example.eventify.databinding.BottomsheetMarkerDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkerDetailsBottomSheet : BaseBottomSheetFragment<BottomsheetMarkerDetailsBinding>(BottomsheetMarkerDetailsBinding::inflate) {

//    val args by navArgs<MarkerDetailsBottomSheetArgs>()

    override fun onViewCreatedLight() {
        NancyToast.makeText(requireContext(), arguments?.getString("id"), NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
//        binding.textVenueNameMarkerDetails.text = args.markerId.toString()
    }
}