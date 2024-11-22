package com.example.eventify.presentation.ui.fragments.profile

import android.content.Intent
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.databinding.FragmentProfileBinding
import com.example.eventify.presentation.ui.activities.OnBoardingActivity

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override fun onViewCreatedLight() {
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.subscriptionFragment)
        }
        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.referralFragment)
        }
        binding.button3.setOnClickListener {
            navigateToOnBoardActivity()
        }
    }
    private fun navigateToOnBoardActivity() {
        val intent = Intent(requireContext(), OnBoardingActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}