package com.example.eventify.presentation.ui.fragments.auth

import android.content.Context
import android.content.Intent
import android.graphics.Color.parseColor
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.common.base.BaseFragment
import com.example.common.utils.NancyToast
import com.example.eventify.BuildConfig
import com.example.eventify.databinding.FragmentLoginBinding
import com.example.eventify.presentation.ui.activities.MainActivity
import com.example.eventify.presentation.viewmodels.LoginViewModel
import com.example.test.TestActivity
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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



        val credentialManager = CredentialManager.create(requireContext())
        val clientId = BuildConfig.WEB_CLIENT_ID

        binding.googleButton.setOnClickListener {
            getGoogleUserData(credentialManager,clientId,requireContext())
        }
    }


    private fun getGoogleUserData(credentialManager: CredentialManager,clientId :String,context : Context){
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(clientId)
            .setAutoSelectEnabled(false)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(context, request)
                val token = GoogleIdTokenCredential.createFrom(result.credential.data)
                val firebaseCredential = GoogleAuthProvider.getCredential(token.idToken,null)
                val e =  Firebase.auth.signInWithCredential(firebaseCredential).await()
                Log.e("NHHH",e.user?.phoneNumber.toString())
            } catch (e: Exception) {
                Snackbar.make(binding.root,"Please sign in to your google account!",Snackbar.LENGTH_SHORT).show()
            }
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
        binding.textForgotPassword.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordFragment)
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