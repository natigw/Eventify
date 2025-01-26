package com.example.eventify.presentation.ui.fragments.auth

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.blockButton
import com.example.common.utils.functions.hideKeyboard
import com.example.common.utils.functions.isValidEmail
import com.example.common.utils.functions.validateInputFieldMeet
import com.example.common.utils.nancyToastInfo
import com.example.common.utils.resetButton
import com.example.eventify.R
import com.example.eventify.databinding.FragmentPasswordResetBinding
import com.example.eventify.presentation.viewmodels.ResetPasswordViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment<FragmentPasswordResetBinding>(FragmentPasswordResetBinding::inflate) {

    private val viewModel by viewModels<ResetPasswordViewModel>()

    override fun onViewCreatedLight() {

    }

    override fun buttonListeners() {
        super.buttonListeners()

        binding.buttonBackPasswordReset.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonSendPasswordReset.setOnClickListener{
            val email = binding.textInputEdittextPasswordReset.text.toString().trim()
            val isEmailValid = validateInputFieldMeet(binding.textInputLayoutPasswordReset, isValidEmail(email), getString(R.string.please_enter_valid_email))
            if (!isEmailValid)
                return@setOnClickListener
            hideKeyboard(binding.root)
            resetUserPassword(email)
        }
    }

    private fun resetUserPassword(email: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            blockButton(
                progressBar = binding.progressBarPasswordReset,
                button = binding.buttonSendPasswordReset
            )
            binding.textInputLayoutPasswordReset.clearFocus()

            val response = viewModel.resetUserPassword(email)
            Log.e("response",response.toString())
            if (response)
                nancyToastInfo(requireContext(), getString(R.string.check_email))
            else {
                Snackbar.make(binding.root,getString(R.string.error_request),Snackbar.LENGTH_SHORT).show()
                binding.textInputLayoutPasswordReset.requestFocus()
            }

            resetButton(
                progressBar = binding.progressBarPasswordReset,
                button = binding.buttonSendPasswordReset,
                buttonText = getString(R.string.send),
                buttonColor = resources.getColor(R.color.eventify_primary)
            )
            binding.textInputEdittextPasswordReset.text = null
        }
    }
}