package com.example.eventify.presentation.ui.fragments.referral

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.common.utils.NancyToast
import com.example.eventify.databinding.FragmentReferralBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferralFragment : BaseFragment<FragmentReferralBinding>(FragmentReferralBinding::inflate) {

    override fun onViewCreatedLight() {
        binding.buttonBackReferral.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonCopyLink.setOnClickListener {
            NancyToast.makeText(requireContext(), "copied!", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
        }
        binding.buttonShareLink.setOnClickListener {
            NancyToast.makeText(requireContext(), "[sharing dialog]", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
        }


        val screenHeight = resources.displayMetrics.heightPixels
        val topMargin = (0.04 * screenHeight).toInt()
        binding.textHeadingReferral.post {
            val params = binding.textHeadingReferral.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = topMargin
            binding.textHeadingReferral.layoutParams = params
        }
    }
}