package com.example.eventify.presentation.ui.fragments.subscription

import android.animation.ObjectAnimator
import android.graphics.Typeface
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.common.utils.NancyToast
import com.example.eventify.databinding.FragmentSubscriptionBinding
import com.example.eventify.presentation.adapters.SubscriptionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.NonCancellable.start

@AndroidEntryPoint
class SubscriptionFragment : BaseFragment<FragmentSubscriptionBinding>(FragmentSubscriptionBinding::inflate) {

//    private val viewmodel by viewModels<SubscriptionViewModel>()

    private val adapter = SubscriptionAdapter(
        currentPackage = "Base",
        isAnnualBilling = true, //viewmodel.switchStateFlow.value
        onClick = {
            NancyToast.makeText(requireContext(), "[navigating payment screen]", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
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