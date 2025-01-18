package com.example.eventify.presentation.ui.fragments.subscription

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.nancyToastSuccess
import com.example.eventify.R
import com.example.eventify.databinding.FragmentSubscriptionBinding
import com.example.eventify.presentation.adapters.SubscriptionAdapter
import com.example.eventify.presentation.viewmodels.SubscriptionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SubscriptionFragment : BaseFragment<FragmentSubscriptionBinding>(FragmentSubscriptionBinding::inflate) {

    private val viewmodel by viewModels<SubscriptionViewModel>()

    private val subscriptionAdapter = SubscriptionAdapter(
//        context = requireContext(),
        currentPackage = "Base",
        isAnnualBilling = false,
        onClick = {
            nancyToastSuccess(requireContext(), getString(R.string.navigating_payment_screen))
        }
    )

    override fun onViewCreatedLight() {
        setLayouts()
        observeChanges()
        setAdapters()
    }

    private fun setLayouts() {
        val screenHeight = resources.displayMetrics.heightPixels
        val topMargin = (0.04 * screenHeight).toInt()
        binding.textHeadingSubs.post {
            val params = binding.textHeadingSubs.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = topMargin
            binding.textHeadingSubs.layoutParams = params
        }
    }

    override fun buttonListeners() {
        super.buttonListeners()

        val typefaceLight = ResourcesCompat.getFont(requireContext(), R.font.inter_light)
        val typefaceBold = ResourcesCompat.getFont(requireContext(), R.font.inter_bold)
        binding.materialSwitch.setOnCheckedChangeListener { _, isChecked ->
            binding.textBillMonthly.typeface = if (!isChecked) typefaceBold else typefaceLight
            binding.textBillAnnually.typeface = if (isChecked) typefaceBold else typefaceLight
            viewmodel.changeSwitchState()
        }

        binding.buttonBackSubs.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setAdapters() {
        binding.rvSubscriptions.adapter = subscriptionAdapter
    }

    private fun observeChanges() {
        lifecycleScope.launch {
            viewmodel.switchState.collectLatest {
                binding.materialSwitch.isChecked = it
                subscriptionAdapter.isAnnualBilling = it // Update adapter with the latest state
                subscriptionAdapter.notifyDataSetChanged() // Notify adapter of data changes if necessary
            }
        }
    }
}