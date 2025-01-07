package com.example.eventify.presentation.ui.fragments.subscription

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.nancyToastSuccess
import com.example.eventify.R
import com.example.eventify.databinding.FragmentSubscriptionBinding
import com.example.eventify.presentation.adapters.SubscriptionAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubscriptionFragment : BaseFragment<FragmentSubscriptionBinding>(FragmentSubscriptionBinding::inflate) {

//    private val viewmodel by viewModels<SubscriptionViewModel>()

    private val adapter = SubscriptionAdapter(
//        context = requireContext(),
        currentPackage = "Base",
        isAnnualBilling = true, //viewmodel.switchStateFlow.value
        onClick = {
            nancyToastSuccess(requireContext(), getString(R.string.navigating_payment_screen))
        }
    )

    override fun onViewCreatedLight() {

        val typefaceLight = ResourcesCompat.getFont(requireContext(), R.font.inter_light)
        val typefaceBold = ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
        binding.materialSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.textBillMonthly.typeface = if (!isChecked) typefaceBold else typefaceLight
            binding.textBillAnnually.typeface = if (isChecked) typefaceBold else typefaceLight
        }

        // Set up RecyclerView with the adapter
        binding.rvSubscriptions.adapter = adapter

//        binding.materialSwitch.setOnCheckedChangeListener { _, _ ->
//            viewmodel.changeState()
//        }

        observeChanges()

        binding.buttonBackSubs.setOnClickListener {
            findNavController().popBackStack()
        }


        val screenHeight = resources.displayMetrics.heightPixels
        val topMargin = (0.04 * screenHeight).toInt()
        binding.textHeadingSubs.post {
            val params = binding.textHeadingSubs.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = topMargin
            binding.textHeadingSubs.layoutParams = params
        }

    }

    private fun observeChanges() {
//        lifecycleScope.launch {
//            viewmodel.switchStateFlow.collect { state ->
//                binding.materialSwitch.isChecked = state
//            }
//        }
    }
}