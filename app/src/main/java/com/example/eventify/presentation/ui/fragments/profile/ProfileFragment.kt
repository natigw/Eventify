package com.example.eventify.presentation.ui.fragments.profile

import com.example.common.base.BaseFragment
import com.example.eventify.NetworkUtils
import com.example.eventify.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {
    override fun onViewCreatedLight() {
        binding.btnLogout.setOnClickListener {
            NetworkUtils.handleLogout(requireContext())
        }
    }
}