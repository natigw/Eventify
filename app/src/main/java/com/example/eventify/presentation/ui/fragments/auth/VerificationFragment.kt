package com.example.eventify.presentation.ui.fragments.auth

import androidx.activity.OnBackPressedCallback
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.blockButton
import com.example.common.utils.nancyToastSuccess
import com.example.common.utils.nancyToastWarning
import com.example.common.utils.resetButton
import com.example.common.utils.showCustomDialog
import com.example.eventify.R
import com.example.eventify.databinding.FragmentVerificationBinding
import com.example.eventify.presentation.viewmodels.VerificationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class VerificationFragment : BaseFragment<FragmentVerificationBinding>(FragmentVerificationBinding::inflate) {

    private val viewModel by viewModels<VerificationViewModel>()

    override fun onViewCreatedLight() {
        observer()
    }

    override fun setUI() {
        binding.textUserEmailVerification.text = viewModel.getUserEmail()
        binding.lottieEnvelopeVerification.setMaxFrame(40)
    }

    override fun buttonListeners() {
        super.buttonListeners()

        val userEmail = viewModel.getUserEmail()
        binding.buttonContinueVerification.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                if (viewModel.isUserVerified(userEmail)) {
                    viewModel.sharedPrefOnBoard.edit {
                        putBoolean("isAuthorized", true)
                    }
                    findNavController().navigate(VerificationFragmentDirections.actionVerificationFragmentToLoginFragment())
                } else {
                    nancyToastWarning(requireContext(), getString(R.string.verify_email))
                }
            }
        }

        binding.buttonResendVerification.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                if (viewModel.resendVerification(userEmail)) {
                    nancyToastSuccess(requireContext(), getString(R.string.check_email))
                } else {
                    nancyToastWarning(requireContext(), getString(R.string.try_again))
                }
            }
        }

        binding.buttonRegisterVerification.setOnClickListener {
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showCustomDialog(
                    context = requireContext(),
                    headingText = getString(R.string.warning),
                    mainText = getString(R.string.verification_warning_text),
                    positiveButtonText = getString(R.string.got_it),
                    neutralButtonText = getString(R.string.cancel_),
                    positiveAction = {
                        findNavController().popBackStack()
                    },
                    cardColor = resources.getColor(R.color.eventify_background_secondary),
                    buttonColor = resources.getColor(R.color.register),
                    textColor = resources.getColor(R.color.eventify_on_background),
                    fontHeading = ResourcesCompat.getFont(requireContext(), R.font.inter_bold),
                    font = ResourcesCompat.getFont(requireContext(), R.font.inter_regular)
                )
            }
        })
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading
                .filter { it != null }
                .collectLatest {
                    if (it!!) {
                        blockButton(
                            progressBar = binding.progressBarVerification,
                            button = binding.buttonContinueVerification
                        )
                    } else {
                        resetButton(
                            progressBar = binding.progressBarVerification,
                            button = binding.buttonContinueVerification,
                            buttonText = getString(R.string.continue_),
                            buttonColor = requireContext().getColor(R.color.register)
                        )
                    }
                }
        }
        viewLifecycleOwner.lifecycleScope.launch {
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
}