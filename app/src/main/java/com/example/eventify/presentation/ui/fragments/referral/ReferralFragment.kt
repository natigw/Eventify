package com.example.eventify.presentation.ui.fragments.referral

import android.content.Intent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.common.utils.copyToClipboard
import com.example.eventify.databinding.FragmentReferralBinding
import com.example.eventify.presentation.viewmodels.ReferralViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferralFragment : BaseFragment<FragmentReferralBinding>(FragmentReferralBinding::inflate) {

    val referralViewModel by viewModels<ReferralViewModel>()

    override fun onViewCreatedLight() {

        val link = "https://picsum.photos/id/237/200/300"  //TODO -> backendden iste

        binding.buttonBackReferral.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonCopyLink.setOnClickListener {
            copyToClipboard(requireContext(), link)
        }
        binding.buttonShareLink.setOnClickListener {
            shareLink(link)
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