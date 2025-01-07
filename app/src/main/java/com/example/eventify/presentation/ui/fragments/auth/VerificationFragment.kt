package com.example.eventify.presentation.ui.fragments.auth

import android.graphics.Color.parseColor
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.NancyToast
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

    val viewModel by viewModels<VerificationViewModel>()

    override fun onViewCreatedLight() {
        observer()

    }

    override fun setUI(){
        val userEmail = viewModel.getUserEmail()
        binding.userEmailTextView.text = userEmail
    }


    override fun buttonListener() {
        super.buttonListener()
        val userEmail = viewModel.getUserEmail()
        binding.verifyButton.setOnClickListener {
            lifecycleScope.launch {
                if(viewModel.isUserVerified(
                        userEmail = userEmail
                )){
                    viewModel.sharedPrefOnBoard.edit {
                        putBoolean("isAuthorized",true)
                    }
                    findNavController().navigate(VerificationFragmentDirections.actionVerificationFragmentToLoginFragment())
                }
                else{
                    nancyToastWarning(requireContext(),getString(R.string.verify_email))
                }
            }
        }

        binding.resendVerificationTextView.setOnClickListener {
            lifecycleScope.launch {
                if(
                    viewModel.resendVerification(userEmail)
                ){
                    binding.registerTextView.isVisible = true
                    nancyToastSuccess(requireContext(),getString(R.string.check_email))
                }
                else {
                    nancyToastWarning(requireContext(), getString(R.string.try_again))
                }
            }
        }
        binding.lottie.setMaxFrame(40)

    }

    private fun observer(){
        lifecycleScope.launch {
            viewModel.isLoading
                .filter { it!=null }
                .collectLatest {
                if(it!!){
                    blockSignupButton()
                }
                else{
                    resetSignupButton()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.resendVerificationState
                .filter { it!=null }
                .collectLatest {
                    if(it!!){
                        binding.resendVerificationTextView.isClickable = false
                        binding.resendVerificationTextView.isFocusable = false
                    }
                    else{
                        binding.resendVerificationTextView.isClickable = true
                        binding.resendVerificationTextView.isFocusable = true
                    }
            }
        }

    }


    private fun blockSignupButton() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            verifyButton.isEnabled = false
            verifyButton.text = null
            verifyButton.setBackgroundColor(parseColor("#FFDADADA"))
        }
    }

    private fun resetSignupButton() {
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            verifyButton.isEnabled = true
            verifyButton.text = getString(R.string.continue_email)
            verifyButton.setBackgroundColor(parseColor("#407BFF"))
        }
    }
}