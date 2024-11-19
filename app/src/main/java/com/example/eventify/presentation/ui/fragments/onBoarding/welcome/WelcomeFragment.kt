package com.example.eventify.presentation.ui.fragments.onBoarding.welcome

import android.content.SharedPreferences
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::inflate) {

    @Inject
    @Named("OnBoardingWelcome")
    lateinit var sharedPrefOnBoard : SharedPreferences

    override fun onViewCreatedLight() {
        if (sharedPrefOnBoard.getBoolean("finished", false)) findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        initViews()
    }

    private fun initViews() {
        val fragmentList = arrayListOf(
            FirstFragment(),
            SecondFragment(),
            ThirdFragment()
        )
        val adapter = WelcomeViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )
        binding.viewPagerWelcome.adapter = adapter
        binding.dotsIndicatorWelcome.attachTo(binding.viewPagerWelcome)
    }
}