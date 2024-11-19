package com.example.eventify.presentation.ui.fragments.onBoarding.welcome

import android.content.Context
import android.content.SharedPreferences
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.databinding.FragmentWelcomeThirdBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ThirdFragment : BaseFragment<FragmentWelcomeThirdBinding>(FragmentWelcomeThirdBinding::inflate) {

    @Inject
    @Named("OnBoardingWelcome")
    lateinit var sharedPrefOnBoard : SharedPreferences

    override fun onViewCreatedLight() {
        initViews()
    }

    private fun initViews() {
        binding.buttonFinish.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
            sharedPrefOnBoard.edit().putBoolean("finished", true).apply()
        }
    }
}