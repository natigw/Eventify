package com.example.eventify.test

import com.example.common.base.BaseFragment
import com.example.common.utils.startShimmer
import com.example.common.utils.stopShimmer
import com.example.eventify.databinding.FragmentShimmerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShimmerFragment : BaseFragment<FragmentShimmerBinding>(FragmentShimmerBinding::inflate) {

    override fun onViewCreatedLight() {
        binding.buttonStartShimmer.setOnClickListener {
            startShimmer(binding.shimmerView)
        }
        binding.buttonStopShimmer.setOnClickListener {
            stopShimmer(binding.shimmerView)
        }
    }

    override fun onResume() {
        super.onResume()
        startShimmer(binding.shimmerView)
    }

    override fun onPause() {
        super.onPause()
        stopShimmer(binding.shimmerView)
    }
}