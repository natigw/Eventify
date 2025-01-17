package com.example.eventify.presentation.ui.fragments.auth.welcomeOnBoard

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.common.base.BaseFragment
import com.example.eventify.R
import com.example.eventify.databinding.FragmentWelcomeSecondBinding
import com.example.eventify.presentation.viewmodels.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SecondFragment : BaseFragment<FragmentWelcomeSecondBinding>(FragmentWelcomeSecondBinding::inflate) {

    private val viewmodel by viewModels<WelcomeViewModel>()

    override fun onViewCreatedLight() {
        binding.buttonSkipSecond.setOnClickListener {
            viewmodel.sharedPrefOnBoard.edit().putBoolean("finished", true).apply()
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