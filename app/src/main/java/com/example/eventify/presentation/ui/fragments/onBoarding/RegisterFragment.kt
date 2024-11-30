package com.example.eventify.presentation.ui.fragments.onBoarding

import android.graphics.Color.parseColor
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.common.utils.NancyToast
import com.example.eventify.common.utils.isValidEmail
import com.example.eventify.data.remote.api.AuthAPI
import com.example.eventify.data.remote.model.register.RequestUserRegistration
import com.example.eventify.databinding.FragmentRegisterBinding
import com.example.eventify.presentation.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {

    private val viewModel by viewModels<RegisterViewModel>()

    override fun onViewCreatedLight() {
        setScrollViewConstraints()
        setInputFieldListeners()

        with(binding) {
            buttonRegister.setOnClickListener {
                val firstname = firstnameRegister.text.toString().trim()
                val lastname = lastnameRegister.text.toString().trim()
                val username = usernameRegister.text.toString().trim()
                val email = emailRegister.text.toString().trim()
                val password = passwordRegister.text.toString().trim()

                if (!checkInputFields(firstname, lastname, username, email, password)) return@setOnClickListener


                lifecycleScope.launch {

                    blockSignupButton()

//                    if (checkIfUserExists(email)) {
//                        NancyToast.makeText(requireContext(), "User already exists!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
//                        resetSignupButton()
//                        return@launch
//                    }

                    if(viewModel.registerUser(
                            firstname,
                            lastname,
                            username,
                            email,
                            password
                    )){
                        NancyToast.makeText(requireContext(), "Registration successful!", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
                    }
                    else{
                        NancyToast.makeText(requireContext(), "Registration failed!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
                    }

//                    try {
////                        registerUser(firstname, lastname, username, email, password)
//
//                        clearInputFields()
//                        NancyToast.makeText(requireContext(), "Registration successful!", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
////                        findNavController().popBackStack()
//                    } catch (e: Exception) {
//                        //TODO -> handle error response here
//                        NancyToast.makeText(requireContext(), "Registration failed!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
//                    } finally {
//                        resetSignupButton()
//                    }
                }
            }
        }

        observeChanges()
    }

    private fun observeChanges() {
        binding.imageBackToLogin.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setScrollViewConstraints() {
        val screenHeight = resources.displayMetrics.heightPixels
        val topMargin = (0.2 * screenHeight).toInt()

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
            NancyToast.makeText(requireContext(), "Please fill the input fields!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
            return false
        }
        if (!isValidEmail(email)) {
            NancyToast.makeText(requireContext(), "Please enter a valid e-mail address!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
            return false
        }

        if (!password.contains("[a-z]".toRegex())) {
            NancyToast.makeText(requireContext(), "Password should contain lowercase letter(s)!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
            return false
        }
        if (!password.contains("[A-Z]".toRegex())) {
            NancyToast.makeText(requireContext(), "Password should contain uppercase letter(s)!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
            return false
        }
        if (!password.contains("[0-9]".toRegex())) {
            NancyToast.makeText(requireContext(), "Password should contain digit(s)!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
            return false
        }
        if (!password.contains("[!\"#$%&'()*+,-./:;\\\\<=>?@\\[\\]^_`{|}~]".toRegex())) {
            NancyToast.makeText(requireContext(), "Password should contain special character(s)!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
            return false
        }

        if (password.length < 8) {
            NancyToast.makeText(requireContext(), "Minimum password length is 8!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
            return false
        }
        if (!binding.checkboxTerms.isChecked) {
            NancyToast.makeText(requireContext(), "Please accept Terms and Conditions!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
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