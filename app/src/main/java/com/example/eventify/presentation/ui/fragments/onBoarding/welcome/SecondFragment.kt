package com.example.eventify.presentation.ui.fragments.onBoarding.welcome

import android.content.SharedPreferences
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.databinding.FragmentWelcomeSecondBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class SecondFragment : BaseFragment<FragmentWelcomeSecondBinding>(FragmentWelcomeSecondBinding::inflate) {

    @Inject
    @Named("OnBoardingWelcome")
    lateinit var sharedPrefOnBoard: SharedPreferences

    override fun onViewCreatedLight() {
        binding.buttonSkipSecond.setOnClickListener {
            sharedPrefOnBoard.edit().putBoolean("finished", true).apply()
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
        initViews()
    }

    private fun initViews() {
        binding.buttonNextSecond.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPagerWelcome)
            viewPager?.currentItem = 2
        }
    }
}