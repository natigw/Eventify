package com.example.eventify.presentation.ui.fragments.profile

import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.crossfadeAppear
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

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()

    private val favoritesAdapter = FavoriteAdapter(
        onClick = {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEventDetailsFragment(it, true))
        }
    )

    override fun onResume() {
        super.onResume()
        viewModel.getFavorites()
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userData
                .filterNotNull()
                .collectLatest {
                    binding.textUsernameProfile.text = it.username
                    binding.textUserFullNameProfile.text = it.fullName
                    crossfadeAppear(binding.textUsernameProfile, duration = 400)
                    crossfadeAppear(binding.textUserFullNameProfile, duration = 400)
                    stopShimmer(binding.shimmerProfile)
                }
        }

        //val subscriptionCode = viewModel...              //from backend
        val subscriptionDisplay = getString(R.string.free) //from backend
        binding.textCurrentPlan.text = subscriptionDisplay

        //val referralCode = viewModel...               //from backend
        val referralDisplay = getString(R.string.done)  //from backend
        binding.textCurrentStatus.text = referralDisplay

        val languageCode = viewModel.sharedPrefLanguage.getString("language", "en")
        val languageDisplay = if (languageCode == "az") getString(R.string.azerbaijani) else if (languageCode == "en") getString(R.string.english) else getString(R.string.russian)
        binding.textCurrentLanguage.text = languageDisplay

        val themeCode = viewModel.sharedPrefTheme.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        val themeDisplay = if (themeCode == 1) getString(R.string.light) else if (themeCode == 2) getString(R.string.dark) else getString(R.string.system)
        binding.textCurrentTheme.text = themeDisplay
    }

    override fun buttonListeners() {
        super.buttonListeners()
        with(binding) {
            cardReferralProfile.setOnClickListener {
                findNavController().navigate(R.id.referralFragment)
            }
            cardSubscriptionProfile.setOnClickListener {
                findNavController().navigate(R.id.subscriptionFragment)
            }

            cardLanguageProfile.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLanguageBottomSheet())
            }
            cardThemeProfile.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToThemeBottomSheet())
            }

            buttonLogoutProfile.setOnClickListener {
                nancyToastInfo(requireContext(), getString(R.string.logout_successful))
                NetworkUtils.handleLogout(requireContext())
            }
        }
    }

    private fun setAdapters() {
        binding.rvFavoritesProfile.adapter = favoritesAdapter
    }

    private fun updateAdapters() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favorites
                .filterNotNull()
                .collectLatest {
                    binding.textFavoritesProfileTEXT.isVisible = it.isNotEmpty()
                    favoritesAdapter.updateAdapter(it)
                    crossfadeAppear(binding.rvFavoritesProfile)
                    stopShimmerGone(binding.shimmerFavoriteProfile)
                    binding.buttonLogoutProfile.visibility = View.VISIBLE
                }
        }
    }
}