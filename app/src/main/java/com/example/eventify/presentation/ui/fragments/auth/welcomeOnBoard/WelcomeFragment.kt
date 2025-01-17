package com.example.eventify.presentation.ui.fragments.auth.welcomeOnBoard

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.eventify.databinding.FragmentWelcomeBinding
import com.example.eventify.presentation.viewmodels.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(FragmentWelcomeBinding::inflate) {

    private val viewmodel by viewModels<WelcomeViewModel>()

    override fun onViewCreatedLight() {
        initViews()
        if (viewmodel.sharedPrefOnBoard.getBoolean("finished", false)){
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
        }
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