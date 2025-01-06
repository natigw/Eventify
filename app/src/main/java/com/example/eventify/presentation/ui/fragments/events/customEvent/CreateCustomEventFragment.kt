package com.example.eventify.presentation.ui.fragments.events.customEvent

import android.icu.util.Calendar
import android.text.TextUtils.substring
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseFragment
import com.example.common.utils.NancyToast
import com.example.common.utils.functions.getLocalTime
import com.example.eventify.R
import com.example.eventify.databinding.FragmentCreateCustomEventBinding
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

@AndroidEntryPoint
class CreateCustomEventFragment : BaseFragment<FragmentCreateCustomEventBinding>(FragmentCreateCustomEventBinding::inflate) {

    private var pickedDate: LocalDate? = null
    private var pickedStartTime: LocalTime? = null
    private var pickedFinishTime: LocalTime? = null

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                Glide.with(binding.imagePictureCreateCustomEvent)
                    .load(uri)
                    .placeholder(R.drawable.placeholder_event)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(binding.imagePictureCreateCustomEvent)
                NancyToast.makeText(requireContext(), "Picture upload successful!", NancyToast.LENGTH_SHORT, NancyToast.SUCCESS, false).show()
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
//        setCurrentDatePicker()
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
                NancyToast.makeText(requireContext(), "Something went wrong!", NancyToast.LENGTH_SHORT, NancyToast.ERROR,false).show()
            }
        }
        binding.buttonDatePickerCCE.setOnClickListener {
            showDatePicker()
        }
        binding.buttonTimePickerStartCCE.setOnClickListener {
            if (binding.buttonDatePickerCCE.text == getString(R.string.event_date)) {
                NancyToast.makeText(requireContext(), "Please specify event date first!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
                return@setOnClickListener
            }
            if (pickedStartTime == null) {
                NancyToast.makeText(requireContext(), "AAAPlease specify event date first!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
                return@setOnClickListener
            }
            showTimePicker(true)
        }
        binding.buttonTimePickerFinishCCE.setOnClickListener {
            if (binding.buttonTimePickerStartCCE.text == getString(R.string.event_start_time)) {
                NancyToast.makeText(requireContext(), "Please specify start time first!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
                return@setOnClickListener
            }
            showTimePicker(false)
        }
    }

//    private fun setCurrentDatePicker() {
//        val todayDate = Date()
//        val dateFormatted = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(todayDate)
//        binding.buttonDatePickerCCE.text = "${dateFormatted}   ${getString(R.string.change)}"
//    }

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
            binding.buttonDatePickerCCE.text = "${formattedDate}    ${getString(R.string.change)}"
            pickedDate = LocalDate.ofEpochDay(selection / (24 * 60 * 60 * 1000))
        }
    }

    private fun showTimePicker(isStart: Boolean) {
        val currentHour = LocalTime.now().hour
        val currentMinute = LocalTime.now().minute
        val timePicker = MaterialTimePicker.Builder()
            .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(if (isStart) currentHour else currentHour+1)
            .setMinute(currentMinute)
            .setTitleText(getString(if (isStart) R.string.event_start_time else R.string.event_finish_time))
            .build()

        timePicker.show(parentFragmentManager, "timePicker")

        timePicker.addOnPositiveButtonClickListener {
            val selectedHour = timePicker.hour
            val selectedMinute = timePicker.minute
            val selectedTime = LocalTime.of(selectedHour, selectedMinute)
//            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            if (isStart) {
                if (pickedDate != null && pickedDate == getLocalTime().toLocalDate() && selectedTime.isBefore(getLocalTime().toLocalTime())) {
                    NancyToast.makeText(requireContext(), "Start time cannot be in the past!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
                    return@addOnPositiveButtonClickListener
                }
                binding.buttonTimePickerStartCCE.text = "${selectedTime}    ${getString(R.string.change)}"
                pickedStartTime = selectedTime
            }
            else {
                if (pickedStartTime != null && selectedTime.isBefore(pickedStartTime)) {
                    NancyToast.makeText(requireContext(), "Finish time cannot be earlier than start time!", NancyToast.LENGTH_SHORT, NancyToast.WARNING, false).show()
                    return@addOnPositiveButtonClickListener
                }
                binding.buttonTimePickerFinishCCE.text = "${selectedTime}    ${getString(R.string.change)}"
                pickedFinishTime = selectedTime
            }
        }
    }
}