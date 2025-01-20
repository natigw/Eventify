package com.example.eventify.presentation.ui.fragments.profile

import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.example.common.base.BaseBottomSheetFragment
import com.example.eventify.databinding.BottomsheetThemeBinding
import com.example.eventify.presentation.viewmodels.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ThemeBottomSheet : BaseBottomSheetFragment<BottomsheetThemeBinding>(BottomsheetThemeBinding::inflate) {

    private val viewmodel by viewModels<ThemeViewModel>()

    override fun onViewCreatedLight() {
        when (viewmodel.chosenTheme) {
            AppCompatDelegate.MODE_NIGHT_NO -> binding.radioButtonLightThemeBSh.isChecked = true
            AppCompatDelegate.MODE_NIGHT_YES -> binding.radioButtonDarkThemeBSh.isChecked = true
            else -> binding.radioButtonSystemThemeBSh.isChecked = true
        }
    }

    override fun buttonListeners() {
        super.buttonListeners()

        binding.radioButtonLightThemeBSh.setOnClickListener {
            viewmodel.chosenTheme = AppCompatDelegate.MODE_NIGHT_NO
        }
        binding.radioButtonDarkThemeBSh.setOnClickListener {
            viewmodel.chosenTheme = AppCompatDelegate.MODE_NIGHT_YES
        }
        binding.radioButtonSystemThemeBSh.setOnClickListener {
            viewmodel.chosenTheme = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        binding.buttonConfirmThemeBSh.setOnClickListener {
            if (viewmodel.chosenTheme != viewmodel.currentTheme)
                changeTheme(viewmodel.chosenTheme)
            dismiss()
        }
    }

    private fun changeTheme(themeMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        viewmodel.sharedPrefTheme.edit().putInt("theme", themeMode).apply()
        requireActivity().recreate()
    }
}
