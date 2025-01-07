package com.example.eventify.presentation.ui.fragments.auth.welcomeOnBoard

import android.content.SharedPreferences
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.common.base.BaseFragment
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
        if (sharedPrefOnBoard.getBoolean("finished", false)){
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
        }

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