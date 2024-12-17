package com.example.eventify.presentation.ui.fragments.profile

import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.common.base.BaseFragment
import com.example.eventify.databinding.FragmentProfileBinding
import com.example.eventify.presentation.ui.activities.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    @Named("UserTokens")
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onViewCreatedLight() {
        binding.button.setOnClickListener {
            findNavController().navigate(R.id.subscriptionFragment)
        }
        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.referralFragment)
        }
        binding.button3.setOnClickListener {
            sharedPreferences.edit{
                clear()
            }
            navigateToOnBoardActivity()
        }
    }
    private fun navigateToOnBoardActivity() {
        val intent = Intent(requireContext(), OnBoardingActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}