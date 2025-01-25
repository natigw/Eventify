package com.example.eventify.presentation.ui.fragments.auth

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.blockButton
import com.example.common.utils.functions.hideKeyboard
import com.example.common.utils.functions.isValidEmail
import com.example.common.utils.functions.validateInputFieldMeet
import com.example.common.utils.nancyToastError
import com.example.common.utils.nancyToastSuccess
import com.example.common.utils.nancyToastWarning
import com.example.common.utils.resetButton
import com.example.eventify.R
import com.example.eventify.databinding.FragmentRegisterBinding
import com.example.eventify.presentation.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onViewCreatedLight() {
        checkAllFields()
        observeChanges()
    }

    private fun observeChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collectLatest {
                with(binding) {
                    if (it) {
                        blockButton(
                            progressBar = progressBarRegister,
                            button = buttonRegister
                        )
                        textInputLayoutFirstnameRegister.clearFocus()
                        textInputLayoutLastnameRegister.clearFocus()
                        textInputLayoutUsernameRegister.clearFocus()
                        textInputLayoutEmailRegister.clearFocus()
                        textInputLayoutPasswordRegister.clearFocus()
                        hideKeyboard(binding.root)
                    } else {
                        resetButton(
                            progressBar = progressBarRegister,
                            button = buttonRegister,
                            buttonText = getString(R.string.register),
                            buttonColor = requireContext().getColor(R.color.register)
                        )
                    }
                }
            }
        }
    }

    override fun buttonListeners() {
        super.buttonListeners()

        binding.buttonBackRegister.setOnClickListener {
            findNavController().popBackStack()
        }
        setInputFieldListeners()
        registerButton()
    }

    private fun registerButton() {
        with(binding) {
            buttonRegister.setOnClickListener {
                val firstname = textInputFirstnameRegister.text.toString().trim()
                val lastname = textInputLastnameRegister.text.toString().trim()
                val username = textInputUsernameRegister.text.toString().trim()
                val email = textInputEmailRegister.text.toString().trim()
                val password = textInputPasswordRegister.text.toString().trim()

                if (!checkInputFields(
                        firstname,
                        lastname,
                        username,
                        email,
                        password
                    )
                ) return@setOnClickListener

                viewLifecycleOwner.lifecycleScope.launch {
                    if (viewModel.registerUser(
                            firstname,
                            lastname,
                            username,
                            email,
                            password
                        )
                    ) {
                        nancyToastSuccess(requireContext(), getString(R.string.register_successful))
                        viewModel.setUserEmail(userEmail = email)
                        clearInputFields()
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToVerificationFragment())
                    } else {
                        nancyToastError(requireContext(), getString(R.string.register_failed))
                    }
                }
            }
        }
    }

    private fun checkInputFields(
        firstname: String,
        lastname: String,
        username: String,
        email: String,
        password: String
    ): Boolean {

        if (firstname.isEmpty() || lastname.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            nancyToastWarning(requireContext(), getString(R.string.fill_all_input_fields))
            return false
        }

        val isEmailValid = validateInputFieldMeet(binding.textInputLayoutEmailRegister, isValidEmail(email), getString(R.string.please_enter_valid_email))

        val passwordErrors = mutableListOf<String>()

        if (!password.contains("[a-z]".toRegex())) passwordErrors.add(getString(R.string.password_should_contain_lowercase))
        if (!password.contains("[A-Z]".toRegex())) passwordErrors.add(getString(R.string.password_should_contain_uppercase))
        if (!password.contains("[0-9]".toRegex())) passwordErrors.add(getString(R.string.password_should_contain_digit))
        if (!password.contains("[!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())) passwordErrors.add(getString(R.string.password_should_contain_special_character))
        if (password.length < 8) passwordErrors.add(getString(R.string.min_password_length_is_8))

        if (passwordErrors.isNotEmpty()) {
            binding.textInputLayoutPasswordRegister.error = passwordErrors.joinToString("\n")
            binding.textInputLayoutPasswordRegister.isErrorEnabled = true
            binding.textInputLayoutPasswordRegister.editText?.requestFocus()
        } else {
            binding.textInputLayoutPasswordRegister.error = null
            binding.textInputLayoutPasswordRegister.isErrorEnabled = false
        }

        if (!isEmailValid || passwordErrors.isNotEmpty()) return false

        if (!binding.checkboxTermsRegister.isChecked) {
            binding.checkboxTermsRegister.setTextColor(requireContext().getColor(R.color.error))
            binding.imageErrorCheckboxTermsRegister.isVisible = true
            nancyToastWarning(requireContext(), getString(R.string.please_accept_terms))
            return false
        } else {
            binding.checkboxTermsRegister.setTextColor(requireContext().getColor(R.color.eventify_on_background))
            binding.imageErrorCheckboxTermsRegister.isVisible = false
        }

        return true
    }

    private fun clearInputFields() {
        binding.apply {
            textInputFirstnameRegister.text = null
            textInputLastnameRegister.text = null
            textInputUsernameRegister.text = null
            textInputEmailRegister.text = null
            textInputPasswordRegister.text = null
            checkboxTermsRegister.isChecked = false
        }
    }

    private fun setInputFieldListeners() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = checkAllFields()

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.apply {
            textInputFirstnameRegister.addTextChangedListener(textWatcher)
            textInputLastnameRegister.addTextChangedListener(textWatcher)
            textInputUsernameRegister.addTextChangedListener(textWatcher)
            textInputEmailRegister.addTextChangedListener(textWatcher)
            textInputPasswordRegister.addTextChangedListener(textWatcher)
            checkboxTermsRegister.setOnCheckedChangeListener { _, _ -> checkAllFields() }
        }
    }

    private fun checkAllFields() {
        val isAllFilled = binding.run {
            textInputFirstnameRegister.text!!.isNotBlank() &&
            textInputLastnameRegister.text!!.isNotBlank() &&
            textInputUsernameRegister.text!!.isNotBlank() &&
            textInputEmailRegister.text!!.isNotBlank() &&
            textInputPasswordRegister.text!!.isNotBlank() &&
            checkboxTermsRegister.isChecked
        }
        binding.buttonRegister.isEnabled = isAllFilled
    }
}