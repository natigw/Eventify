package com.example.eventify.presentation.ui.fragments.auth

import android.content.Intent
import android.graphics.Color.parseColor
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.edit
import androidx.credentials.CredentialManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.navigateUp
import com.example.common.base.BaseFragment
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
        loginButton()
        setConstraints()
        checkUser()


    }

    override fun buttonListener() {
        super.buttonListener()

        val credentialManager = CredentialManager.create(requireContext())
        val clientId = BuildConfig.WEB_CLIENT_ID

        binding.googleButton.setOnClickListener {
            lifecycleScope.launch {
                GoogleUtils.getGoogleUserData(
                    credentialManager = credentialManager,
                    clientId = clientId,
                    requireContext()
                )
                if(viewModel.linkGoogleAccount()){
                    Intent(requireContext(),MainActivity::class.java).also {
                        startActivity(it)
                    }
                }
            }
        }

        binding.textForgotPassword.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.navigating_help_page))
        }

        binding.textDontHaveAccount.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        binding.button4.setOnClickListener {
            navigateToMainActivity()
        }

        binding.buttonTEsts.setOnClickListener {
            navigateToTestActivity()
        }

        binding.textForgotPassword.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment())
        }
    }



    private fun checkUser(){
        val userEmail = viewModel.sharedPrefUserTokens.getString("userEmail",null)
        val condition = viewModel.sharedPrefUserTokens.getBoolean("isAuthorized",false)
        if(userEmail != null && !condition){
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToVerificationFragment())
        }else{
            binding.emailLogin.setText(userEmail)
        }
    }


    private fun setConstraints(){
        val screenHeight = resources.displayMetrics.heightPixels
        val topMargin = (0.08 * screenHeight).toInt()

        binding.textSignIn.post {
            val params = binding.textSignIn.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = topMargin
            binding.textSignIn.layoutParams = params

        }
    }



    private fun loginButton() {
        binding.buttonLogin.setOnClickListener {

            val username = binding.emailLogin.text.toString().trim()
            val password = binding.passwordLogin.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                nancyToastWarning(requireContext(), getString(R.string.please_fill_all_fields))
                return@setOnClickListener
            }

            lifecycleScope.launch {
                if(viewModel.loginUser(username = username,password = password)){
                    clearInputFields()
                    nancyToastSuccess(requireContext(), getString(R.string.login_successful))
                    viewModel.removeVerifiedUserEmail()
                    Intent(requireContext(), MainActivity::class.java).also {
                        startActivity(it)
                        requireActivity().finish()
                    }

                }
                else{
                    nancyToastWarning(requireContext(), getString(R.string.login_unsuccessful))
                }


            }

        }
    }

    fun toastLoginErrors(){
        lifecycleScope.launch {
            viewModel.errorMessagesState
                .filter { it != null }
                .collectLatest {
                    nancyToastWarning(requireContext(), it)
            }
        }
    }

    private fun observer(){
        lifecycleScope.launch {
            viewModel.isLoading.collectLatest {
                if(it){
                    blockLoginButton()
                }
                else{
                    resetLoginButton()
                }
            }
        }
    }

    private fun blockLoginButton() {
        binding.apply {
            progressBarLogin.visibility = View.VISIBLE
            buttonLogin.isEnabled = false
            buttonLogin.text = null
            buttonLogin.setBackgroundColor(parseColor("#FFDADADA"))
        }
    }

    private fun resetLoginButton() {
        binding.apply {
            progressBarLogin.visibility = View.INVISIBLE
            buttonLogin.isEnabled = true
            buttonLogin.text = getString(R.string.login)
            buttonLogin.setBackgroundColor(parseColor("#F44336"))
        }
    }

    private fun clearInputFields() {
        binding.apply {
            emailLogin.text = null
            passwordLogin.text = null
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