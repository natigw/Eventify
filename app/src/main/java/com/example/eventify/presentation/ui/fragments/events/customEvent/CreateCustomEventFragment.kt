package com.example.eventify.presentation.ui.fragments.events.customEvent

import com.example.common.base.BaseFragment
import com.example.common.utils.functions.getLocalTime
import com.example.eventify.R
import com.example.eventify.databinding.FragmentCreateCustomEventBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class CreateCustomEventFragment : BaseFragment<FragmentCreateCustomEventBinding>(FragmentCreateCustomEventBinding::inflate) {
    override fun onViewCreatedLight() {
        setTodayDate()
        binding.buttonDatePickerCCE.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setTodayDate() {
        val todayDate = Date()
        val dateFormatted = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(todayDate)
        binding.buttonDatePickerCCE.text = "${dateFormatted}   ${getString(R.string.change)}"
    }

    private fun showDatePicker() {

        val today = MaterialDatePicker.todayInUtcMilliseconds()
        // Create constraints to block future dates
        val constraintsBuilder = CalendarConstraints.Builder()
            .setEnd(today)

        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(getString(R.string.event_date))
            .setCalendarConstraints(constraintsBuilder.build())
            //.setTheme(R.style.CustomDatePickerTheme)
            .build()

        datePicker.show(parentFragmentManager, "datePicker")

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(selection))
            binding.buttonDatePickerCCE.text = selectedDate
        }
    }
}