package com.example.eventify.presentation.ui.fragments.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import com.example.common.base.BaseFragment
import com.example.common.utils.NancyToast
import com.example.common.utils.nancyToastInfo
import com.example.eventify.NetworkUtils
import com.example.eventify.R
import com.example.eventify.databinding.FragmentProfileBinding
import com.example.eventify.presentation.ui.activities.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    @Named("UserTokens")
    @Inject
    lateinit var sharedPrefUserToken: SharedPreferences

    @Inject
    @Named("LanguageChoice")
    lateinit var sharedPrefLanguage: SharedPreferences

    override fun onViewCreatedLight() {
        binding.buttonLogoutProfile.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.logout_successful))
            NetworkUtils.handleLogout(requireContext())
        }

        binding.btnChangeLanguageAZ.setOnClickListener {
            changeLanguage("az")
        }
        binding.btnChangeLanguageEN.setOnClickListener {
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
        sharedPrefLanguage.edit().putString("language", languageCode).apply()
        requireActivity().recreate()
    }
}