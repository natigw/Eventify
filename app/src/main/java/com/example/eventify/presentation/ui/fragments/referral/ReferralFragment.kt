package com.example.eventify.presentation.ui.fragments.referral

import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.common.utils.copyToClipboard
import com.example.eventify.data.remote.api.EventAPI
import com.example.eventify.databinding.FragmentReferralBinding
import com.example.eventify.presentation.viewmodels.ReferralViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ReferralFragment : BaseFragment<FragmentReferralBinding>(FragmentReferralBinding::inflate) {

    @Inject
    lateinit var eventApi: EventAPI


    val referralViewModel by viewModels<ReferralViewModel>()

    override fun onViewCreatedLight() {

        val link = "https://picsum.photos/id/237/200/300"  //TODO -> backendden iste

        binding.buttonCopyLink.setOnClickListener {
            copyToClipboard(requireContext(), link)
        }
        binding.buttonShareLink.setOnClickListener {
            shareLink(link)
        }


        val numberOfLinkSent = 2  //TODO -> backendden iste
        lifecycleScope.launch {
            var chosenEvent = ""
            referralViewModel.eventsState
                .filter { it.isNotEmpty() }
                .collectLatest {
                chosenEvent = it.random().name
                binding.textGetTicketDescriptionReferral.text = "Almost there! Refer ${3 - numberOfLinkSent} more friend to get a free ticket for \"$chosenEvent\" event."
                binding.textGetTicketDescriptionReferral.text = "Congratulations! You got a ticket for \"$chosenEvent\" event. Please check your ticket box!"
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
                    binding.textGetTicketDescriptionReferral.text = "Congratulations! You got a ticket for \"$chosenEvent\" event. Please check your ticket box!"
                }
            }
        }


        binding.buttonBackReferral.setOnClickListener {
            findNavController().popBackStack()
        }

        setConstraints()
    }

    private fun shareLink(text: String, subject: String? = null) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain" // Specify the MIME type for text
            putExtra(Intent.EXTRA_TEXT, text) // Add the text to share
            subject?.let {
                putExtra(Intent.EXTRA_SUBJECT, it) // Optional: Add a subject
            }
        }
        // Show a chooser to let the user pick the app
        val chooser = Intent.createChooser(intent, "Share via")
        requireContext().startActivity(chooser)
    }

    private fun setConstraints() {
        val screenHeight = resources.displayMetrics.heightPixels
        val topMargin = (0.04 * screenHeight).toInt()
        binding.textHeadingReferral.post {
            val params = binding.textHeadingReferral.layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = topMargin
            binding.textHeadingReferral.layoutParams = params
        }
    }
}