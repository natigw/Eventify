package com.example.eventify.presentation.ui.fragments.auth.welcomeOnBoard

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.eventify.R
import com.example.eventify.databinding.FragmentWelcomeThirdBinding
import com.example.eventify.presentation.viewmodels.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThirdFragment : BaseFragment<FragmentWelcomeThirdBinding>(FragmentWelcomeThirdBinding::inflate) {

    private val viewmodel by viewModels<WelcomeViewModel>()

    override fun onViewCreatedLight() {
        initViews()
    }

    private fun initViews() {
        binding.buttonFinishThird.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
            viewmodel.sharedPrefOnBoard.edit().putBoolean("finished", true).apply()
        }
    }
}