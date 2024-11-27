package com.example.eventify.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.eventify.data.remote.api.AuthAPI
import com.example.eventify.data.remote.model.register.RequestUserRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val api: AuthAPI
) : ViewModel() {

//    private fun registerUser (
//        firstname: String,
//        lastname: String,
//        username: String,
//        email: String,
//        password: String
//    ) {
//        lifecycleScope.launch {
//            api.registerUser(
//                RequestUserRegistration(
//                    username = username,
//                    email = email,
//                    password = password,
//                    firstName = firstname,
//                    lastName = lastname,
//                    isOrganizer = 0
//                )
//            )
//        }
//    }
}