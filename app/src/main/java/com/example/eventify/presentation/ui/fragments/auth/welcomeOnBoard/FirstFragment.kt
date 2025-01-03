package com.example.eventify.presentation.ui.fragments.auth.welcomeOnBoard

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.core.app.ActivityCompat.recreate
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.eventify.R
import com.example.common.base.BaseFragment
import com.example.eventify.databinding.FragmentWelcomeFirstBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class FirstFragment : BaseFragment<FragmentWelcomeFirstBinding>(FragmentWelcomeFirstBinding::inflate) {

    @Inject
    @Named("OnBoardingWelcome")
    lateinit var sharedPrefOnBoard : SharedPreferences

    @Inject
    @Named("LanguageChoice")
    lateinit var sharedPrefLanguage : SharedPreferences

    override fun onViewCreatedLight() {
        binding.buttonSkipFirst.setOnClickListener {
            sharedPrefOnBoard.edit().putBoolean("finished", true).apply()
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
        initViews()

        binding.buttonWelcomeLanguageAZ.setOnClickListener {
            changeLanguage("az")
        }
        binding.buttonWelcomeLanguageEN.setOnClickListener {
            changeLanguage("en")
        }
    }

    private fun initViews() {
        binding.buttonLetsGoFirst.setOnClickListener {
            val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPagerWelcome)
            viewPager?.currentItem = 1
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
        sharedPrefLanguage.edit().putString("language", languageCode).apply()
        requireParentFragment().requireActivity().recreate()  //TODO -> bunu hell et crash olur
    }
}