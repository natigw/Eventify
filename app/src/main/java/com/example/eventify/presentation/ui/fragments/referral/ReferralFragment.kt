package com.example.eventify.presentation.ui.fragments.referral

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.common.base.BaseFragment
import com.example.common.utils.functions.copyToClipboard
import com.example.common.utils.navigateWithAnimationFade
import com.example.eventify.R
import com.example.eventify.databinding.FragmentReferralBinding
import com.example.eventify.presentation.viewmodels.ReferralViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReferralFragment : BaseFragment<FragmentReferralBinding>(FragmentReferralBinding::inflate) {

    private val referralViewModel by viewModels<ReferralViewModel>()

    override fun onViewCreatedLight() {


        binding.card1Referral.visibility = View.GONE


        lateinit var chosenEvent: String

        val numberOfLinkSent = 2  //TODO -> backendden
        try {
            viewLifecycleOwner.lifecycleScope.launch {
//            val chosenEvent = eventApi.getAllEvents().body()?.random()?.event?.title   //TODO -> backendden
                referralViewModel.eventsState
                    .filter { it.isNotEmpty() }
                    .collectLatest {
                        chosenEvent = it.random().name
                        binding.textGetTicketDescriptionReferral.text =
                            getString(R.string.referral_description_1)+"${3 - numberOfLinkSent}"+getString(R.string.referral_description_2)+"$chosenEvent"+getString(R.string.referral_description_3)
                        binding.textGetTicketDescriptionReferral.text =
                            getString(R.string.referral_congrats_1)+"$chosenEvent"+getString(R.string.referral_congrats_2)
                    }

                binding.progressReferral.progress = 33 * numberOfLinkSent
                when (numberOfLinkSent) {
                    1 -> {
                        binding.imageProgress1.setImageResource(R.drawable.check_progress_green)
                    }

                    2 -> {
                        binding.imageProgress1.setImageResource(R.drawable.check_progress_green)
                        binding.imageProgress2.setImageResource(R.drawable.check_progress_green)
                    }

                    3 -> {
                        binding.imageProgress1.setImageResource(R.drawable.check_progress_green)
                        binding.imageProgress2.setImageResource(R.drawable.check_progress_green)
                        binding.imageProgress3.setImageResource(R.drawable.check_progress_green)
                        binding.textGetTicketDescriptionReferral.text =
                            getString(R.string.referral_congrats_1)+"$chosenEvent"+getString(R.string.referral_congrats_2)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("network", e.toString())
        }
    }

    override fun buttonListeners() {
        super.buttonListeners()

        binding.buttonBackReferral.setOnClickListener {
            navigateWithAnimationFade(findNavController(), destination = R.id.profileFragment, popUpTo = R.id.referralFragment)
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateWithAnimationFade(findNavController(), destination = R.id.profileFragment, popUpTo = R.id.referralFragment)
                }
            }
        )

        val link = "https://haseventify.vercel.app"  //TODO -> backendden

        binding.buttonCopyLink.setOnClickListener {
            copyToClipboard(requireContext(), link)
        }
        binding.buttonShareLink.setOnClickListener {
            shareLink(link)
        }
    }

    private fun shareLink(text: String, subject: String? = null) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            subject?.let {
                putExtra(Intent.EXTRA_SUBJECT, it) //Optional: Add a subject
            }
        }
        // Show a chooser to let the user pick the app
        val chooser = Intent.createChooser(intent, "Share via")
        requireContext().startActivity(chooser)
    }
}