package com.example.eventify.presentation.ui.fragments.subscription

import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.databinding.FragmentSubscriptionBinding
import com.example.eventify.presentation.adapters.SubscriptionAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionFragment : BaseFragment<FragmentSubscriptionBinding>(FragmentSubscriptionBinding::inflate) {

//    private val viewmodel by viewModels<SubscriptionViewModel>()

    private val adapter = SubscriptionAdapter(
        currentPackage = "Base",
        isAnnualBilling = false//viewmodel.switchStateFlow.value
    )

    override fun onViewCreatedLight() {

        var isAnnualBilling = binding.materialSwitch.isChecked
        val typefaceLight = ResourcesCompat.getFont(requireContext(), R.font.inter_light)
        val typefaceBold = ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
        binding.materialSwitch.setOnCheckedChangeListener { _, _ ->
            isAnnualBilling = !isAnnualBilling
            if (isAnnualBilling) {
                binding.textBillMonthly.typeface = typefaceLight
                binding.textBillAnnually.typeface = typefaceBold
            } else {
                binding.textBillMonthly.typeface = typefaceBold
                binding.textBillAnnually.typeface = typefaceLight
            }
        }

        // Set up RecyclerView with the adapter
        binding.rvSubscriptions.adapter = adapter

//        binding.materialSwitch.setOnCheckedChangeListener { _, _ ->
//            viewmodel.changeState()
//        }
    }

    override fun observeChanges() {
//        lifecycleScope.launch {
//            viewmodel.switchStateFlow.collect { state ->
//                binding.materialSwitch.isChecked = state
//            }
//        }
    }
}