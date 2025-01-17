package com.example.eventify.presentation.ui.fragments.profile

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.nancyToastInfo
import com.example.common.utils.startShimmer
import com.example.common.utils.stopShimmer
import com.example.common.utils.stopShimmerGone
import com.example.eventify.NetworkUtils
import com.example.eventify.R
import com.example.eventify.databinding.FragmentProfileBinding
import com.example.eventify.presentation.adapters.FavoriteAdapter
import com.example.eventify.presentation.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()

    private val favoritesAdapter = FavoriteAdapter(
        onClick = {
            //TODO -> qayitmaq isteyende event fragmentde gedir duzelt
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEventDetailsFragment(it, true))
        }
    )

    override fun onResume() {
        super.onResume()
        if (viewModel.userData.value == null)
            startShimmer(binding.shimmerProfile)
        if (viewModel.favorites.value == null)
            startShimmer(binding.shimmerFavoriteProfile)
    }

    override fun onViewCreatedLight() {
        setAdapters()
        updateAdapters()
    }

    override fun onPause() {
        super.onPause()
        stopShimmer(binding.shimmerProfile)
        stopShimmerGone(binding.shimmerFavoriteProfile)
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

    private fun setAdapters() {
        binding.rvFavoritesProfile.adapter = favoritesAdapter
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
            viewModel.favorites
                .filterNotNull()
                .collectLatest {
                    binding.textNoFavoritesTEXTProfile.isVisible = it.isEmpty()
                    favoritesAdapter.updateAdapter(it)
                    stopShimmerGone(binding.shimmerFavoriteProfile)
                    binding.buttonLogoutProfile.visibility = View.VISIBLE
                }
        }
    }
}