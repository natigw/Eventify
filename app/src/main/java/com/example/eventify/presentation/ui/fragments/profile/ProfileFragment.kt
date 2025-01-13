package com.example.eventify.presentation.ui.fragments.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.NancyToast
import com.example.common.utils.nancyToastInfo
import com.example.common.utils.startShimmer
import com.example.common.utils.stopShimmer
import com.example.data.remote.api.EventAPI
import com.example.eventify.NetworkUtils
import com.example.eventify.R
import com.example.eventify.databinding.FragmentProfileBinding
import com.example.eventify.presentation.ui.activities.OnBoardingActivity
import com.example.eventify.presentation.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onResume() {
        super.onResume()
        if (viewModel.userData.value == null)
            startShimmer(binding.shimmerProfile)
    }

    override fun onViewCreatedLight() {

    }

    override fun onPause() {
        super.onPause()
        stopShimmer(binding.shimmerProfile)
    }

    override fun setUI() {
        lifecycleScope.launch {
            viewModel.userData
                .filterNotNull()
                .collectLatest {
                    binding.textUsernameProfile.text = it.username
                    binding.textUserFullNameProfile.text = it.fullName
                    stopShimmer(binding.shimmerProfile)
                }
        }
    }

    override fun buttonListener() {
        super.buttonListener()
        binding.btnReferral.setOnClickListener {
            findNavController().navigate(R.id.referralFragment)
        }
        binding.btnSubs.setOnClickListener {
            findNavController().navigate(R.id.subscriptionFragment)
        }
        binding.buttonChangeLanguageAZ.setOnClickListener {
            changeLanguage("az")
        }
        binding.buttonChangeLanguageEN.setOnClickListener {
            changeLanguage("en")
        }
        binding.buttonChangeLanguageRU.setOnClickListener {
            nancyToastInfo(requireContext(), "Aqsin tercume eder")
            //changeLanguage("ru")
        }
        binding.buttonLogoutProfile.setOnClickListener {
            nancyToastInfo(requireContext(), getString(R.string.logout_successful))
            NetworkUtils.handleLogout(requireContext())
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
        viewModel.sharedPrefLanguage.edit().putString("language", languageCode).apply()
        requireActivity().recreate()
    }
}