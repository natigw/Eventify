package com.example.eventify.presentation.ui.fragments.auth

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.common.base.BaseFragment
import com.example.common.utils.NancyToast
import com.example.eventify.R
import com.example.eventify.databinding.PasswordResetBinding
import com.example.eventify.presentation.viewmodels.ResetPasswordViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment<PasswordResetBinding>(PasswordResetBinding::inflate) {
    val viewModel by viewModels<ResetPasswordViewModel>()


    override fun onViewCreatedLight() {
        binding.buttonSend.setOnClickListener{
            resetUserPassword()
        }
    }

    fun resetUserPassword(){
        lifecycleScope.launch {
            val response = viewModel.resetUserPassword(binding.resetPassword.text.toString())
            Log.e("response",response.toString())
            if (response){
                Toast.makeText(requireContext(), getString(R.string.check_email), Toast.LENGTH_SHORT).show()
            }
            else{
                Snackbar.make(binding.root,getString(R.string.error_request),Snackbar.LENGTH_SHORT).show()
            }
        }

    }

}