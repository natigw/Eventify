package com.example.eventify.presentation.ui.fragments.auth

import android.graphics.Color.parseColor
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.functions.isValidEmail
import com.example.common.utils.nancyToastError
import com.example.common.utils.nancyToastSuccess
import com.example.common.utils.nancyToastWarning
import com.example.eventify.R
import com.example.eventify.databinding.FragmentRegisterBinding
import com.example.eventify.presentation.viewmodels.OnBoardingSharedViewModel
import com.example.eventify.presentation.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel by viewModels<RegisterViewModel>()
    private val sharedViewModel by activityViewModels<OnBoardingSharedViewModel>()

    override fun onViewCreatedLight() {
        setScrollViewConstraints()
        setInputFieldListeners()
        registerButton()
        observeChanges()
    }

    private fun registerButton(){
        with(binding) {
            buttonRegister.setOnClickListener {
                val firstname = firstnameRegister.text.toString().trim()
                val lastname = lastnameRegister.text.toString().trim()
                val username = usernameRegister.text.toString().trim()
                val email = emailRegister.text.toString().trim()
                val password = passwordRegister.text.toString().trim()

                if (!checkInputFields(firstname, lastname, username, email, password)) return@setOnClickListener

                lifecycleScope.launch {
                    if(viewModel.registerUser(
                            firstname,
                            lastname,
                            username,
                            email,
                            password
                        )){
                        nancyToastSuccess(requireContext(), getString(R.string.register_successful))
                        sharedViewModel.setFromRegisterScreen()
                        sharedViewModel.setUserEmail(email)
                        clearInputFields()
                        findNavController().popBackStack()
                    }
                    else{
                        nancyToastError(requireContext(), getString(R.string.register_failed))
                    }
                }
            }
        }
    }
    private fun observeChanges() {
        binding.imageBackToLogin.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            viewModel.isLoading.collectLatest {
                if(it){
                    blockSignupButton()
                }
                else{
                    resetSignupButton()
                }
            }
        }
    }

    private fun setScrollViewConstraints() {
        val screenHeight = resources.displayMetrics.heightPixels
        val topMargin = (0.08 * screenHeight).toInt()

        binding.textSignUp.post {
            val params = binding.textSignUp.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = topMargin
            binding.textSignUp.layoutParams = params
        }

        val topMarginArrow = (0.04 * screenHeight).toInt()
        val paramsArrow = binding.imageBackToLogin.layoutParams as ConstraintLayout.LayoutParams
        paramsArrow.topMargin = topMarginArrow
        binding.imageBackToLogin.layoutParams = paramsArrow
    }

    private fun checkInputFields(firstname: String, lastname: String, username: String, email: String, password: String) : Boolean {
        if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            nancyToastWarning(requireContext(), getString(R.string.fill_all_input_fields))
            return false
        }
        if (!isValidEmail(email)) {
            nancyToastError(requireContext(), getString(R.string.please_enter_valid_email))
            return false
        }

        if (!password.contains("[a-z]".toRegex())) {
            nancyToastWarning(requireContext(), getString(R.string.password_should_contain_lowercase))
            return false
        }
        if (!password.contains("[A-Z]".toRegex())) {
            nancyToastWarning(requireContext(), getString(R.string.password_should_contain_uppercase))
            return false
        }
        if (!password.contains("[0-9]".toRegex())) {
            nancyToastWarning(requireContext(), getString(R.string.password_should_contain_digit))
            return false
        }
        if (!password.contains("[!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())) {
            nancyToastWarning(requireContext(), getString(R.string.password_should_contain_special_character))
            return false
        }

        if (password.length < 8) {
            nancyToastWarning(requireContext(), getString(R.string.min_password_length_is_8))
            return false
        }
        if (!binding.checkboxTerms.isChecked) {
            nancyToastWarning(requireContext(), getString(R.string.please_accept_terms))
            return false
        }
        return true
    }

    private fun blockSignupButton() {
        binding.apply {
            progressBarRegister.visibility = View.VISIBLE
            buttonRegister.isEnabled = false
            buttonRegister.text = null
            buttonRegister.setBackgroundColor(parseColor("#FFDADADA"))
            checkboxTerms.isEnabled = false
        }
    }

    private fun resetSignupButton() {
        binding.apply {
            progressBarRegister.visibility = View.INVISIBLE
            buttonRegister.isEnabled = true
            buttonRegister.text = "Register"
            buttonRegister.setBackgroundColor(parseColor("#407BFF"))
            checkboxTerms.isEnabled = true
        }
    }

    private fun clearInputFields() {
        binding.apply {
            firstnameRegister.text = null
            lastnameRegister.text = null
            usernameRegister.text = null
            emailRegister.text = null
            passwordRegister.text = null
            checkboxTerms.isChecked = false
        }
    }

    private fun setInputFieldListeners() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = checkAllFields()

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.apply {
            firstnameRegister.addTextChangedListener(textWatcher)
            lastnameRegister.addTextChangedListener(textWatcher)
            usernameRegister.addTextChangedListener(textWatcher)
            emailRegister.addTextChangedListener(textWatcher)
            passwordRegister.addTextChangedListener(textWatcher)
            checkboxTerms.setOnCheckedChangeListener { _, _ -> checkAllFields() }
        }
    }

    private fun checkAllFields() {
        val isAllFilled = binding.run {
            firstnameRegister.text!!.isNotBlank() &&
            lastnameRegister.text!!.isNotBlank() &&
            usernameRegister.text!!.isNotBlank() &&
            emailRegister.text!!.isNotBlank() &&
            passwordRegister.text!!.isNotBlank() &&
            checkboxTerms.isChecked
        }
        binding.buttonRegister.isEnabled = isAllFilled
    }
}