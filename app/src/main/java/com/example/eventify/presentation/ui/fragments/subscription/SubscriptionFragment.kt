package com.example.eventify.presentation.ui.fragments.subscription

import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.nancyToastSuccess
import com.example.common.utils.navigateWithAnimationFade
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
        currentPackage = "Base",
        isAnnual = false,
        onClick = {
            nancyToastSuccess(requireContext(), it + getString(R.string.navigating_payment_screen))
        }

    )

    override fun onViewCreatedLight() {
        observeChanges()
        setAdapters()


        //TEMPORARILY INVISIBLE
        binding.textBillMonthly.isVisible = false
        binding.textBillAnnually.isVisible = false
        binding.switchSubscription.isVisible = false
    }

    override fun buttonListeners() {
        super.buttonListeners()

        binding.switchSubscription.setOnClickListener {
            viewmodel.changeSwitchState()
        }
        binding.buttonBackSubs.setOnClickListener {
            navigateWithAnimationFade(findNavController(), destination = R.id.profileFragment, popUpTo = R.id.subscriptionFragment)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateWithAnimationFade(findNavController(), destination = R.id.profileFragment, popUpTo = R.id.subscriptionFragment)
                }
            }
        )
    }

    private fun setAdapters() {
        binding.rvSubscriptions.adapter = subscriptionAdapter
    }

    private fun observeChanges() {

        val typefaceLight = ResourcesCompat.getFont(requireContext(), R.font.inter_light)
        val typefaceBold = ResourcesCompat.getFont(requireContext(), R.font.inter_bold)

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.isAnnual.collectLatest {
                binding.switchSubscription.isChecked = it
                binding.textBillMonthly.typeface = if (!it) typefaceBold else typefaceLight
                binding.textBillAnnually.typeface = if (it) typefaceBold else typefaceLight

                subscriptionAdapter.isAnnual = it
                subscriptionAdapter.notifyDataSetChanged()
            }
        }
    }
}