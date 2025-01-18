package com.example.eventify.presentation.ui.fragments.auth.welcomeOnBoard

import android.content.Context
import android.content.res.Configuration
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.common.base.BaseFragment
import com.example.eventify.R
import com.example.eventify.databinding.FragmentWelcomeFirstBinding
import com.example.eventify.presentation.viewmodels.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class FirstFragment : BaseFragment<FragmentWelcomeFirstBinding>(FragmentWelcomeFirstBinding::inflate) {

    private val viewmodel by viewModels<WelcomeViewModel>()

    override fun onViewCreatedLight() { }

    override fun buttonListeners() {
        super.buttonListeners()
        binding.buttonSkipFirst.setOnClickListener {
            viewmodel.sharedPrefOnBoard.edit().putBoolean("finished", true).apply()
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
        binding.buttonGetStartedFirst.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPagerWelcome)
            viewPager?.currentItem = 1
        }
        binding.buttonWelcomeLanguageAZ.setOnClickListener {
            changeLanguage("az")
        }
        binding.buttonWelcomeLanguageEN.setOnClickListener {
            changeLanguage("en")
        }
    }

    private fun setAppLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
    private fun changeLanguage(languageCode: String) {
        setAppLocale(requireContext(), languageCode)
        viewmodel.sharedPrefLanguage.edit().putString("language", languageCode).apply()
        requireParentFragment().requireActivity().recreate()  //TODO -> bunu hell et crash olur
    }
}