package com.example.eventify.presentation.ui.fragments.events.customEvent

import android.icu.util.Calendar
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseFragment
import com.example.common.utils.functions.getLocalTime
import com.example.common.utils.functions.validateInputFieldEmpty
import com.example.common.utils.nancyToastError
import com.example.common.utils.nancyToastSuccess
import com.example.common.utils.nancyToastWarning
import com.example.eventify.R
import com.example.eventify.databinding.FragmentCreateCustomEventBinding
import com.example.eventify.presentation.viewmodels.CreateCustomEventViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

@AndroidEntryPoint
class CreateCustomEventFragment : BaseFragment<FragmentCreateCustomEventBinding>(FragmentCreateCustomEventBinding::inflate) {

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
        viewModelObserver()
        clickListeners()
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
            if (viewmodel.pickedDate.value == null) {
                nancyToastWarning(requireContext(), getString(R.string.please_specify_event_date_first))
                ariseErrorIcon(binding.imageErrorDateCCE, true)
                ariseErrorButton(binding.buttonDatePickerCCE, true)
                return@setOnClickListener
            }
            showTimePicker(true)
        }
        binding.buttonTimePickerFinishCCE.setOnClickListener {
            if (viewmodel.pickedStartTime.value == null) {
                nancyToastWarning(requireContext(), getString(R.string.please_specify_event_start_time_first))
                ariseErrorIcon(binding.imageErrorStartTimeCCE, true)
                ariseErrorButton(binding.buttonTimePickerStartCCE, true)
                return@setOnClickListener
            }
            showTimePicker(false)
        }
        binding.buttonCreateCCE.setOnClickListener {
            val title = binding.textInputEdittextTitleCCE.text.toString().trim()
            val description = binding.textInputEdittextDescriptionCCE.text.toString().trim()
            val eventType = binding.autoCompleteTextEventType.text.toString().trim()

            val isEventTypeValid = validateInputFieldEmpty(binding.textInputLayoutEventTypeCCE, eventType, getString(R.string.please_specify_event_type))
            val isDescriptionValid = validateInputFieldEmpty(binding.textInputLayoutEventDescriptionCCE, description, getString(R.string.please_add_event_description))
            val isTitleValid = validateInputFieldEmpty(binding.textInputLayoutEventTitleCCE, title, getString(R.string.please_add_event_title))

            ariseErrorIcon(binding.imageErrorDateCCE, viewmodel.pickedDate.value == null)
            ariseErrorIcon(binding.imageErrorStartTimeCCE, viewmodel.pickedStartTime.value == null)
            ariseErrorIcon(binding.imageErrorFinishTimeCCE, viewmodel.pickedFinishTime.value == null)
            ariseErrorButton(binding.buttonDatePickerCCE, viewmodel.pickedDate.value == null)
            ariseErrorButton(binding.buttonTimePickerStartCCE, viewmodel.pickedStartTime.value == null)
            ariseErrorButton(binding.buttonTimePickerFinishCCE, viewmodel.pickedFinishTime.value == null)

            if (!(isTitleValid and isDescriptionValid and isEventTypeValid)) {
                return@setOnClickListener
            }
            if (viewmodel.pickedDate.value == null || viewmodel.pickedStartTime.value == null || viewmodel.pickedFinishTime.value == null) {
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
            viewmodel.pickedDate.value = LocalDate.ofEpochDay(selection / (24 * 60 * 60 * 1000))

            if (viewmodel.pickedDate.value == getLocalTime().toLocalDate() && viewmodel.pickedStartTime.value != null && viewmodel.pickedStartTime.value!!.isBefore(getLocalTime().toLocalTime())) {
                nancyToastWarning(requireContext(), getString(R.string.please_reconsider_event_times))
                ariseErrorIcon(binding.imageErrorStartTimeCCE, true)
                ariseErrorButton(binding.buttonTimePickerStartCCE, true)
                ariseErrorIcon(binding.imageErrorFinishTimeCCE, true)
                ariseErrorButton(binding.buttonTimePickerFinishCCE, true)
                viewmodel.pickedStartTime.update { null }
                viewmodel.pickedFinishTime.update { null }
            } else {
                ariseErrorIcon(binding.imageErrorDateCCE, false)
                ariseErrorButton(binding.buttonDatePickerCCE, false)
            }
        }
    }

    private fun showTimePicker(isStart: Boolean) {
        val currentHour = LocalTime.now().hour
        val currentMinute = LocalTime.now().minute
        val timePicker = MaterialTimePicker.Builder()
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(if (isStart) currentHour else viewmodel.pickedStartTime.value!!.plusHours(2).hour)
            .setMinute(if (isStart) currentMinute else viewmodel.pickedStartTime.value!!.plusHours(2).minute)
            .setTitleText(getString(if (isStart) R.string.event_start_time else R.string.event_finish_time))
            .build()

        timePicker.show(parentFragmentManager, "timePicker")

        timePicker.addOnPositiveButtonClickListener {
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            val selectedTime = LocalTime.of(selectedHour, selectedMinute)
            if (isStart) {
                if (viewmodel.pickedDate.value == getLocalTime().toLocalDate() && selectedTime.plusMinutes(1).isBefore(getLocalTime().toLocalTime())) {
                    nancyToastWarning(requireContext(), getString(R.string.start_time_cannot_be_past))
                    return@addOnPositiveButtonClickListener
                }
                if (viewmodel.pickedFinishTime.value != null && selectedTime.isAfter(viewmodel.pickedFinishTime.value)) {
                    nancyToastWarning(requireContext(), getString(R.string.please_reconsider_finish_time))
                    binding.buttonTimePickerFinishCCE.text = getString(R.string.specify_event_finish_time)
                    ariseErrorIcon(binding.imageErrorFinishTimeCCE, true)
                    ariseErrorButton(binding.buttonTimePickerFinishCCE, true)
                    viewmodel.pickedFinishTime.update { null }
                } else {
                    ariseErrorIcon(binding.imageErrorStartTimeCCE, false)
                    ariseErrorButton(binding.buttonTimePickerStartCCE, false)
                }
                viewmodel.pickedStartTime.update { selectedTime }
            }
            else {
                if (selectedTime.isBefore(viewmodel.pickedStartTime.value)) {
                    nancyToastWarning(requireContext(), getString(R.string.finish_time_cannot_be_earlier))
                    ariseErrorIcon(binding.imageErrorFinishTimeCCE, true)
                    ariseErrorButton(binding.buttonTimePickerFinishCCE, true)
                    return@addOnPositiveButtonClickListener
                } else {
                    ariseErrorIcon(binding.imageErrorFinishTimeCCE, false)
                    ariseErrorButton(binding.buttonTimePickerFinishCCE, false)
                }
                viewmodel.pickedFinishTime.update { selectedTime }
            }
        }
    }

    private fun ariseErrorIcon(view: ImageView, isError: Boolean) {
        view.isVisible = isError
    }
    private fun ariseErrorButton(button: Button, isError: Boolean) {
        button.setBackgroundColor(requireContext().getColor(if (isError) R.color.error else R.color.eventify_primary))
    }

    private fun viewModelObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.pickedDate.collectLatest {
                binding.buttonDatePickerCCE.text = if (it == null) getString(R.string.specify_event_date) else "$it    ${getString(R.string.change)}"
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.pickedStartTime.collectLatest {
                binding.buttonTimePickerStartCCE.text = if (it == null) getString(R.string.specify_event_start_time) else "$it    ${getString(R.string.change)}"
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.pickedFinishTime.collectLatest {
                binding.buttonTimePickerFinishCCE.text = if (it == null) getString(R.string.specify_event_finish_time) else "$it    ${getString(R.string.change)}"
            }
        }
    }
}

//NOTE: IF USER CANNOT TO SET AN EVENT THAT WILL CONTINUE LATE NIGHT (AFTER 00:00)