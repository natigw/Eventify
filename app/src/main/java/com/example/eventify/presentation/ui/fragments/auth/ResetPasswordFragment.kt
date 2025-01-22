package com.example.eventify.presentation.ui.fragments.auth

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.nancyToastInfo
import com.example.eventify.R
import com.example.eventify.databinding.PasswordResetBinding
import com.example.eventify.presentation.viewmodels.ResetPasswordViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment<PasswordResetBinding>(PasswordResetBinding::inflate) {

    private val viewModel by viewModels<ResetPasswordViewModel>()

    override fun onViewCreatedLight() {

    }

    override fun buttonListeners() {
        super.buttonListeners()
        binding.buttonSendPasswordReset.setOnClickListener{
            resetUserPassword()
        }
        binding.buttonBackPasswordReset.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun resetUserPassword(){
        viewLifecycleOwner.lifecycleScope.launch {
            val response = viewModel.resetUserPassword(binding.textInputEdittextPasswordReset.text.toString())
            Log.e("response",response.toString())
            if (response){
                nancyToastInfo(requireContext(), getString(R.string.check_email))
            }
            else{
                Snackbar.make(binding.root,getString(R.string.error_request),Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}