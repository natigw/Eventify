package com.example.eventify.presentation.ui.fragments.events.customEvent

import android.icu.util.Calendar
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseFragment
import com.example.common.utils.functions.getLocalTime
import com.example.common.utils.nancyToastError
import com.example.common.utils.nancyToastSuccess
import com.example.common.utils.nancyToastWarning
import com.example.data.remote.api.EventAPI
import com.example.eventify.R
import com.example.eventify.databinding.FragmentCreateCustomEventBinding
import com.example.eventify.presentation.viewmodels.CreateCustomEventViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class CreateCustomEventFragment : BaseFragment<FragmentCreateCustomEventBinding>(FragmentCreateCustomEventBinding::inflate) {

    @Inject
    lateinit var api: EventAPI

    private val viewmodel by viewModels<CreateCustomEventViewModel>()

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                Glide.with(binding.imagePictureCreateCustomEvent)
                    .load(uri)
                    .placeholder(R.drawable.placeholder_event)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.imagePictureCreateCustomEvent)
                nancyToastSuccess(requireContext(), getString(R.string.picture_upload_successful))
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    override fun onResume() {
        super.onResume()
        val eventTypes = resources.getStringArray(R.array.event_types)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_event_type_item, eventTypes)
        binding.autoCompleteTextEventType.setAdapter(arrayAdapter)
    }

    override fun onViewCreatedLight() {
        clickListeners()
    }

    override fun setUI() {
        with(binding) {
            buttonDatePickerCCE.text = if (viewmodel.pickedDate == null) getString(R.string.specify_event_date) else "${viewmodel.pickedDate}    ${getString(R.string.change)}"
            buttonTimePickerStartCCE.text = if (viewmodel.pickedStartTime == null) getString(R.string.specify_event_start_time) else "${viewmodel.pickedStartTime}    ${getString(R.string.change)}"
            buttonTimePickerFinishCCE.text = if (viewmodel.pickedFinishTime == null) getString(R.string.specify_event_finish_time) else "${viewmodel.pickedFinishTime}    ${getString(R.string.change)}"
        }
    }

    private fun clickListeners() {
        binding.buttonBackCCE.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonAddPictureCreateCustomEvent.setOnClickListener {
            try {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } catch (e: Exception) {
                Log.e("PhotoPicker", "PHOTOPICKER FAILED!")
                nancyToastError(requireContext(), getString(R.string.something_went_wrong))
            }
        }
        binding.buttonDatePickerCCE.setOnClickListener {
            showDatePicker()
        }
        binding.buttonTimePickerStartCCE.setOnClickListener {
            if (viewmodel.pickedDate == null) {
                nancyToastWarning(requireContext(), getString(R.string.please_specify_event_date_first))
                return@setOnClickListener
            }
            showTimePicker(true)
        }
        binding.buttonTimePickerFinishCCE.setOnClickListener {
            if (viewmodel.pickedStartTime == null) {
                nancyToastWarning(requireContext(), getString(R.string.please_specify_event_start_time_first))
                return@setOnClickListener
            }
            showTimePicker(false)
        }
        binding.buttonCreateCCE.setOnClickListener {
            val title = binding.titleCCE.text.toString().trim()
            val description = binding.descriptionCCE.text.toString().trim()
            val eventType = binding.autoCompleteTextEventType.text.toString().trim()
            if (title.isEmpty()) {
                nancyToastWarning(requireContext(), getString(R.string.please_add_event_title))
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                nancyToastWarning(requireContext(), getString(R.string.please_add_event_description))
                return@setOnClickListener
            }
            if (eventType.isEmpty()) {
                nancyToastWarning(requireContext(), getString(R.string.please_specify_event_type))
                return@setOnClickListener
            }
            if (viewmodel.pickedDate == null || viewmodel.pickedStartTime == null || viewmodel.pickedFinishTime == null) {
                nancyToastWarning(requireContext(), getString(R.string.please_specify_event_all_timing_details))
                return@setOnClickListener
            }
            nancyToastSuccess(requireContext(), getString(R.string.custom_event_creation_successful))
            findNavController().popBackStack()
        }
    }

    private fun showDatePicker() {
        // Create constraints to block past dates
        val constraintsBuilder = CalendarConstraints.Builder()
            .setFirstDayOfWeek(Calendar.MONDAY)
            .setValidator(DateValidatorPointForward.now())

        val datePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(getString(R.string.event_date))
            .setCalendarConstraints(constraintsBuilder.build())
            .setTheme(R.style.MaterialCalendarTheme)
            .build()

        datePicker.show(parentFragmentManager, "datePicker")

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = Date(selection)
            val formattedDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(selectedDate)
            binding.buttonDatePickerCCE.text = "$formattedDate    ${getString(R.string.change)}"
            viewmodel.pickedDate = LocalDate.ofEpochDay(selection / (24 * 60 * 60 * 1000))

            if (viewmodel.pickedDate == getLocalTime().toLocalDate() && viewmodel.pickedStartTime != null && viewmodel.pickedStartTime!!.isBefore(getLocalTime().toLocalTime())) {
                nancyToastWarning(requireContext(), getString(R.string.please_reconsider_event_times))
                binding.buttonTimePickerStartCCE.text = getString(R.string.specify_event_start_time)
                binding.buttonTimePickerFinishCCE.text = getString(R.string.specify_event_finish_time)
                viewmodel.pickedStartTime = null
                viewmodel.pickedFinishTime = null
            }
        }
    }

    private fun showTimePicker(isStart: Boolean) {
        val currentHour = LocalTime.now().hour
        val currentMinute = LocalTime.now().minute
        val timePicker = MaterialTimePicker.Builder()
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(if (isStart) currentHour else viewmodel.pickedStartTime!!.plusHours(2).hour)
            .setMinute(if (isStart) currentMinute else viewmodel.pickedStartTime!!.plusHours(2).minute)
            .setTitleText(getString(if (isStart) R.string.event_start_time else R.string.event_finish_time))
            .build()

        timePicker.show(parentFragmentManager, "timePicker")

        timePicker.addOnPositiveButtonClickListener {
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            val selectedTime = LocalTime.of(selectedHour, selectedMinute)
            if (isStart) {
                if (viewmodel.pickedDate == getLocalTime().toLocalDate() && selectedTime.plusMinutes(1).isBefore(getLocalTime().toLocalTime())) {
                    nancyToastWarning(requireContext(), getString(R.string.start_time_cannot_be_past))
                    return@addOnPositiveButtonClickListener
                }
                if (viewmodel.pickedFinishTime != null && selectedTime.isAfter(viewmodel.pickedFinishTime)) {
                    nancyToastWarning(requireContext(), getString(R.string.please_reconsider_finish_time))
                    binding.buttonTimePickerFinishCCE.text = getString(R.string.specify_event_finish_time)
                    viewmodel.pickedFinishTime = null
                }
                binding.buttonTimePickerStartCCE.text = "$selectedTime    ${getString(R.string.change)}"
                viewmodel.pickedStartTime = selectedTime
            }
            else {
                if (selectedTime.isBefore(viewmodel.pickedStartTime)) {
                    nancyToastWarning(requireContext(), getString(R.string.finish_time_cannot_be_earlier))
                    return@addOnPositiveButtonClickListener
                }
                binding.buttonTimePickerFinishCCE.text = "$selectedTime    ${getString(R.string.change)}"
                viewmodel.pickedFinishTime = selectedTime
            }
        }
    }
}

//NOTE: IF USER CANNOT TO SET AN EVENT THAT WILL CONTINUE LATE NIGHT (AFTER 00:00)