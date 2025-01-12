package com.example.eventify.presentation.ui.fragments.auth

import android.view.View
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.nancyToastSuccess
import com.example.common.utils.nancyToastWarning
import com.example.eventify.R
import com.example.eventify.databinding.VerificationLayoutBinding
import com.example.eventify.presentation.viewmodels.VerificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VerificationFragment : BaseFragment<VerificationLayoutBinding>(VerificationLayoutBinding::inflate) {

    private val viewModel by viewModels<VerificationViewModel>()

    override fun onViewCreatedLight() {
        observer()
    }

    override fun setUI() {
        binding.textUserEmailVerification.text = viewModel.getUserEmail()
    }

    override fun buttonListener() {
        super.buttonListener()
        val userEmail = viewModel.getUserEmail()
        binding.buttonVerify.setOnClickListener {
            lifecycleScope.launch {
                if (viewModel.isUserVerified(userEmail = userEmail)) {
                    viewModel.sharedPrefOnBoard.edit {
                        putBoolean("isAuthorized", true)
                    }
                    findNavController().navigate(VerificationFragmentDirections.actionVerificationFragmentToLoginFragment())
                } else {
                    nancyToastWarning(requireContext(), getString(R.string.verify_email))
                }
            }
        }

        binding.textResendVerification.setOnClickListener {
            lifecycleScope.launch {
                if (viewModel.resendVerification(userEmail)) {
                    binding.textWrongEmailRegisterVerification.isVisible = true
                    nancyToastSuccess(requireContext(), getString(R.string.check_email))
                } else {
                    nancyToastWarning(requireContext(), getString(R.string.try_again))
                }
            }
        }
        binding.lottieEnvelopeVerification.setMaxFrame(40)
    }

    private fun observer() {
        lifecycleScope.launch {
            viewModel.isLoading
                .filter { it != null }
                .collectLatest {
                    if (it!!) {
                        blockSignupButton()
                    } else {
                        resetSignupButton()
                    }
                }
        }
        lifecycleScope.launch {
            viewModel.resendVerificationState
                .filter { it != null }
                .collectLatest {
                    if (it!!) {
                        binding.textResendVerification.isClickable = false
                        binding.textResendVerification.isFocusable = false
                    } else {
                        binding.textResendVerification.isClickable = true
                        binding.textResendVerification.isFocusable = true
                    }
                }
        }
    }


    private fun blockSignupButton() {
        binding.apply {
            progressBarVerification.visibility = View.VISIBLE
            buttonVerify.isEnabled = false
            buttonVerify.text = null
            buttonVerify.setBackgroundColor(requireContext().getColor(R.color.button_disabled))
        }
    }

    private fun resetSignupButton() {
        binding.apply {
            progressBarVerification.visibility = View.INVISIBLE
            buttonVerify.isEnabled = true
            buttonVerify.text = getString(R.string.continue_email)
            buttonVerify.setBackgroundColor(requireContext().getColor(R.color.register))
        }
    }
}