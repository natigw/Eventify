package com.example.eventify.presentation.ui.fragments.auth

import android.content.Intent
import android.graphics.Color.parseColor
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.credentials.CredentialManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.functions.isValidEmail
import com.example.common.utils.functions.validateInputFieldEmpty
import com.example.common.utils.functions.validateInputFieldMeet
import com.example.common.utils.nancyToastInfo
import com.example.common.utils.nancyToastSuccess
import com.example.common.utils.nancyToastWarning
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

        ////TODO -> ??????????????????
        if (viewModel.sharedPrefOnBoard.getBoolean("finished", false)) binding.textWelcomeLogin.visibility = View.VISIBLE

        observer()
        setConstraints()
        checkUser()

        binding.buttonTest.setOnClickListener {
            navigateToTestActivity()
        }
    }

    override fun buttonListener() {
        super.buttonListener()

        val credentialManager = CredentialManager.create(requireContext())
        val clientId = BuildConfig.WEB_CLIENT_ID

        binding.buttonGoogle.setOnClickListener {
            lifecycleScope.launch {
                GoogleUtils.getGoogleUserData(
                    credentialManager = credentialManager,
                    clientId = clientId,
                    requireContext()
                )
                if (viewModel.linkGoogleAccount()) {
                    Intent(requireContext(), MainActivity::class.java).also {
                        startActivity(it)
                        requireActivity().finish()
                    }
                    requireActivity().finish()
                }
            }
        }

        binding.textDontHaveAccountLoginTEXT.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        binding.textForgotPasswordLogin.setOnClickListener {
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

            lifecycleScope.launch {
                if (viewModel.loginUser(username = username, password = password)) {
                    clearInputFields()
                    nancyToastSuccess(requireContext(), getString(R.string.login_successful))
                    navigateToMainActivity()
                } else {
                    nancyToastWarning(requireContext(), getString(R.string.login_unsuccessful))
                }
            }
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

    private fun setConstraints() {
        val screenHeight = resources.displayMetrics.heightPixels
        val topMargin = (0.08 * screenHeight).toInt()

        binding.textSignInTEXT.post {
            val params = binding.textSignInTEXT.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = topMargin
            binding.textSignInTEXT.layoutParams = params
        }
    }


    private fun observer() {
        lifecycleScope.launch {
            viewModel.isLoadingGoogle
                .filter { it != null }
                .collectLatest {
                    if (it!!) blockGoogleButton()
                    else resetGoogleButton()
                }
        }

        lifecycleScope.launch {
            viewModel.isLoading
                .filter { it != null }
                .collectLatest {
                    if (it!!) {
                        blockLoginButton()
                    } else {
                        resetLoginButton()
                    }
                }
        }
    }

    private fun blockLoginButton() {
        binding.progressBarLogin.visibility = View.VISIBLE
        binding.buttonLogin.apply {
            isEnabled = false
            text = null
            setBackgroundColor(requireContext().getColor(R.color.button_disabled))
        }
    }

    private fun resetLoginButton() {
        binding.progressBarLogin.visibility = View.INVISIBLE
        binding.buttonLogin.apply {
            isEnabled = true
            text = getString(R.string.login)
            setBackgroundColor(requireContext().getColor(R.color.login))
        }
    }

    private fun blockGoogleButton() {
        binding.apply {
            progressBarGoogle.isVisible = true
            buttonGoogle.isEnabled = false
            buttonLogin.isEnabled = false
            buttonGoogle.text = null
        }
    }

    private fun resetGoogleButton() {
        binding.apply {
            progressBarGoogle.isVisible = false
            buttonGoogle.isEnabled = true
            buttonLogin.isEnabled = true
            buttonGoogle.text = getString(R.string.continue_with_google)
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