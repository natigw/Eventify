package com.example.eventify.presentation.ui.fragments.onBoarding

import android.content.Intent
import android.graphics.Color.parseColor
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.common.base.BaseFragment
import com.example.common.utils.NancyToast
import com.example.eventify.databinding.FragmentLoginBinding
import com.example.eventify.presentation.ui.activities.MainActivity
import com.example.eventify.presentation.viewmodels.LoginViewModel
import com.example.test.TestActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {


    val viewModel by viewModels<LoginViewModel>()



    override fun onViewCreatedLight() {

        ////??????????????????
        if (viewModel.sharedPrefOnBoard.getBoolean("finished", false)) binding.textWelcomeLogin.visibility = View.VISIBLE


        binding.textDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        observer()
        loginButton()
        setConstraints()
        observeChanges()


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
    private fun observeChanges() {
        binding.textForgotPassword.setOnClickListener {
            NancyToast.makeText(requireContext(), "[navigating to help page]", NancyToast.LENGTH_SHORT, NancyToast.INFO, false).show()
        }
        binding.textDontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.button4.setOnClickListener {
            navigateToMainActivity()
        }
        binding.buttonTEsts.setOnClickListener {
            navigateToTestActivity()
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
                if(viewModel.loginUser(username = username,password = password)){
                    clearInputFields()
                    NancyToast.makeText(requireContext(), "Login Successful", NancyToast.LENGTH_SHORT,NancyToast.SUCCESS,false).show()

                    Intent(requireContext(), MainActivity::class.java).also {
                        startActivity(it)
                    }

                }
                else{

                    NancyToast.makeText(requireContext(), "Login Unsuccessful", NancyToast.LENGTH_SHORT,NancyToast.WARNING,false).show()
                }


            }

        }
    }

    fun toastLoginErrors(){
        lifecycleScope.launch {
            viewModel.errorMessagesState
                .filter { it != null }
                .collectLatest {
                    NancyToast.makeText(requireContext(), it, NancyToast.LENGTH_SHORT, NancyToast.WARNING,false).show()
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
    private fun navigateToTestActivity() {
        val intent = Intent(requireContext(), TestActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}