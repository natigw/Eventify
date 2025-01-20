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

    override fun buttonListeners() {
        super.buttonListeners()
        with(binding) {
            btnReferral.setOnClickListener {
                findNavController().navigate(R.id.referralFragment)
            }
            btnSubs.setOnClickListener {
                findNavController().navigate(R.id.subscriptionFragment)
            }
            buttonLogoutProfile.setOnClickListener {
                nancyToastInfo(requireContext(), getString(R.string.logout_successful))
                NetworkUtils.handleLogout(requireContext())
            }



            carta.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLanguageBottomSheet())
            }
            textChangeThemeTEXT.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToThemeBottomSheet())
            }
        }
    }

    private fun setAdapters() {
        binding.rvFavoritesProfile.adapter = favoritesAdapter
    }

    private fun updateAdapters() {
        lifecycleScope.launch {
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