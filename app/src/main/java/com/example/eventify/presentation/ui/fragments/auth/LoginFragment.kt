package com.example.eventify.presentation.ui.fragments.auth

import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.credentials.CredentialManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.blockButton
import com.example.common.utils.functions.hideKeyboard
import com.example.common.utils.functions.validateInputFieldEmpty
import com.example.common.utils.nancyToastSuccess
import com.example.common.utils.nancyToastWarning
import com.example.common.utils.resetButton
import com.example.data.remote.thirdpartyregister.GoogleUtils
import com.example.eventify.BuildConfig
import com.example.eventify.R
import com.example.eventify.databinding.FragmentLoginBinding
import com.example.eventify.presentation.ui.activities.MainActivity
import com.example.eventify.presentation.viewmodels.LoginViewModel
import com.example.eventify.test.TestActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreatedLight() {
        observer()
        checkUser()
    }

    override fun buttonListeners() {
        super.buttonListeners()
        val credentialManager = CredentialManager.create(requireContext())
        val clientId = BuildConfig.WEB_CLIENT_ID

        binding.buttonGoogle.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                GoogleUtils.getGoogleUserData(
                    credentialManager = credentialManager,
                    clientId = clientId,
                    requireContext()
                )
                if (viewModel.linkGoogleAccount()) {
                    navigateToMainActivity()
                    nancyToastSuccess(requireContext(), getString(R.string.login_successful))
                }
                else{
                    nancyToastWarning(requireContext(), getString(R.string.login_unsuccessful))
                }
            }
        }

        binding.buttonRegisterLogin.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        binding.buttonForgotPasswordLogin.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment())
        }

        binding.buttonLogin.setOnClickListener {

            val username = binding.textInputUsernameLogin.text.toString().trim()
            val password = binding.textInputPasswordLogin.text.toString().trim()

            val isUsernameFilled = validateInputFieldEmpty(binding.textInputLayoutUsernameLogin, username, getString(R.string.please_enter_username))
            val isPasswordFilled = validateInputFieldEmpty(binding.textInputLayoutPasswordLogin, password, getString(R.string.please_enter_password))

            if (!(isUsernameFilled and isPasswordFilled)) {
                return@setOnClickListener
            }

            viewLifecycleOwner.lifecycleScope.launch {
                if (viewModel.loginUser(username = username, password = password)) {
                    clearInputFields()
                    nancyToastSuccess(requireContext(), getString(R.string.login_successful))
                    navigateToMainActivity()
                } else {
                    nancyToastWarning(requireContext(), getString(R.string.login_unsuccessful))
                }
            }
        }

        binding.buttonTest.setOnClickListener {
            navigateToTestActivity()
        }
    }

    private fun checkUser() {
        val userEmail = viewModel.sharedPrefUserTokens.getString("userEmail", null)
        val condition = viewModel.sharedPrefUserTokens.getBoolean("isAuthorized", false)
        if (userEmail != null && !condition) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToVerificationFragment())
        } else {
            binding.textInputUsernameLogin.setText(userEmail)
        }
    }

    private fun observer() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoadingGoogle
                .filter { it != null }
                .collectLatest {
                    if (it!!)
                        blockButton(
                            progressBar = binding.progressBarGoogle,
                            button = binding.buttonGoogle
                        )
                    else
                        resetButton(
                            progressBar = binding.progressBarGoogle,
                            button = binding.buttonGoogle,
                            buttonText = getString(R.string.continue_with_google),
                            buttonColor = requireContext().getColor(R.color.eventify_background_secondary)
                        )
                }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading
                .filter { it != null }
                .collectLatest {
                    with(binding) {
                        if (it!!) {
                            blockButton(
                                progressBar = progressBarLogin,
                                button = buttonLogin
                            )
                            textInputLayoutUsernameLogin.clearFocus()
                            textInputLayoutPasswordLogin.clearFocus()
                            hideKeyboard(binding.root)
                        } else {
                            resetButton(
                                progressBar = progressBarLogin,
                                button = buttonLogin,
                                buttonText = getString(R.string.login),
                                buttonColor = requireContext().getColor(R.color.login)
                            )
                        }
                    }
                }
        }
    }

    private fun clearInputFields() {
        binding.apply {
            textInputUsernameLogin.text = null
            textInputPasswordLogin.text = null
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun navigateToTestActivity() {
        val intent = Intent(requireContext(), TestActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}