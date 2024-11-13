package com.example.eventify.presentation.ui.fragments.onBoarding

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color.parseColor
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.common.utils.NancyToast
import com.example.eventify.data.remote.api.EventifyAPI
import com.example.eventify.data.remote.model.userToken.RequestUserToken
import com.example.eventify.databinding.FragmentLoginBinding
import com.example.eventify.presentation.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    @Inject
    @Named("OnBoardingWelcome")
    lateinit var sharedPrefOnBoard: SharedPreferences

    @Inject
    @Named("UserLoggedIn")
    lateinit var sharedPrefLoggedIn: SharedPreferences

    @Inject
    lateinit var api: EventifyAPI

    override fun onViewCreatedLight() {

        if (sharedPrefOnBoard.getBoolean("finished", false)) binding.textWelcomeLogin.visibility = View.VISIBLE

        binding.textDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        val screenHeight = resources.displayMetrics.heightPixels
        val topMargin = (0.2 * screenHeight).toInt()
        val params = binding.textSignIn.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = topMargin
        binding.textSignIn.layoutParams = params

        loginButton()
    }

    override fun observeChanges() {
        binding.textForgotPassword.setOnClickListener {
            NancyToast.makeText(requireContext(), "[navigating to help page]", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()
        }
        binding.textDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun loginButton() {
        binding.buttonLogin.setOnClickListener {

            val username = binding.emailLogin.text.toString().trim()
            val password = binding.passwordLogin.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                NancyToast.makeText(requireContext(), "Please fill the fields!", NancyToast.LENGTH_SHORT, NancyToast.WARNING,false).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                blockLoginButton()
                try {
                    lifecycleScope.launch {
                        val tokenResponse = api.requestUserToken(
                            RequestUserToken(
                                username = username,
                                password = password
                            )
                        )
                        if (api.verifyUserToken(tokenResponse.accessToken).message == "Token is valid") {
                            NancyToast.makeText(requireContext(), "ok", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
                            //userAuthentication(email, password)
//                            val editor = sharedPrefLoggedIn.edit()                //bu hisse userAuthentication icerisindedi
//                            editor.putString("username", username)
//                            editor.putString("email", email)
//                            editor.putBoolean("status_login", true)
//                            editor.apply()
                        }
                        else {
                            NancyToast.makeText(requireContext(), "invalid", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
                        }
                    }
                }
                catch (e: Exception) {
                    Log.e("Network exception", "login error")
                }
                finally {
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
            buttonLogin.text = "Login"
            buttonLogin.setBackgroundColor(parseColor("#F44336"))
        }
    }

    private fun clearInputFields() {
        binding.apply {
            emailLogin.text = null
            passwordLogin.text = null
        }
    }

//    private suspend fun userAuthentication(email: String, password: String) {
//        try {
//            val document = firestore.collection("users").document(email).get().await()
//            if (document.exists()) {
//                val data = document.toObject(FirestoreUser::class.java)
//                if (data != null && password == data.password) {
//                    val editor = shprefLogon.edit()
//                    editor.putString("username", data.username)
//                    editor.putString("email", data.email)
//                    editor.putBoolean("status_loggedin", true)
//                    editor.putString("entrypin", data.entrypin)
//                    editor.apply()
//                    clearInputFields()
//                    //authUserInTMDB(data.apikey)
//                    NancyToast.makeText(requireContext(), "Login successful!", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
//                    navigateToMainActivity()
//                } else {
//                    NancyToast.makeText(requireContext(), "Invalid password!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
//                }
//            } else {
//                NancyToast.makeText(requireContext(), "User doesn't exist!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
//            }
//        } catch (e: Exception) {
//            NancyToast.makeText(requireContext(), "Auth failed!", NancyToast.LENGTH_SHORT, NancyToast.ERROR, false).show()
//        }
//    }

    private fun navigateToMainActivity() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}