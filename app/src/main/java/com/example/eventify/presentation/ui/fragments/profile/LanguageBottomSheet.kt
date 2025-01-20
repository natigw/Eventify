package com.example.eventify.presentation.ui.fragments.profile

import android.content.Context
import android.content.res.Configuration
import androidx.fragment.app.viewModels
import com.example.common.base.BaseBottomSheetFragment
import com.example.eventify.databinding.BottomsheetLanguageBinding
import com.example.eventify.presentation.viewmodels.LanguageViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class LanguageBottomSheet : BaseBottomSheetFragment<BottomsheetLanguageBinding>(BottomsheetLanguageBinding::inflate) {

    private val viewmodel by viewModels<LanguageViewModel>()

    override fun onViewCreatedLight() {
        when (viewmodel.chosenLanguage) {
            "az" -> binding.radioButtonAZLanguageBSh.isChecked = true
            "ru" -> binding.radioButtonRULanguageBSh.isChecked = true
            else -> binding.radioButtonENLanguageBSh.isChecked = true
        }
    }

    override fun buttonListeners() {
        super.buttonListeners()

        binding.radioButtonAZLanguageBSh.setOnClickListener {
            viewmodel.chosenLanguage = "az"
        }
        binding.radioButtonENLanguageBSh.setOnClickListener {
            viewmodel.chosenLanguage = "en"
        }
        binding.radioButtonRULanguageBSh.setOnClickListener {
            viewmodel.chosenLanguage = "ru"
        }

        binding.buttonConfirmLanguageBSh.setOnClickListener {
            if (viewmodel.chosenLanguage != viewmodel.currentLanguage)
                changeLanguage(viewmodel.chosenLanguage)
            dismiss()
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
        viewmodel.sharedPrefLanguage.edit().putString("language", languageCode).apply()
        requireActivity().recreate()
    }
}