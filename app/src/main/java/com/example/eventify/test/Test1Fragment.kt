package com.example.eventify.test

import android.net.Uri
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseFragment
import com.example.common.utils.NancyToast
import com.example.common.utils.nancyToastError
import com.example.common.utils.nancyToastInfo
import com.example.common.utils.nancyToastSuccess
import com.example.data.remote.api.EventAPI
import com.example.data.remote.model.events.FileUploadGetLinkResponse
import com.example.eventify.R
import com.example.eventify.databinding.FragmentTest1Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class Test1Fragment : BaseFragment<FragmentTest1Binding>(FragmentTest1Binding::inflate) {

    @Inject
    lateinit var api: EventAPI

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                uploadImageToApi(uri)
            } else {
                nancyToastInfo(requireContext(), getString(R.string.no_image_selected), NancyToast.LENGTH_LONG)
            }
        }

    override fun onViewCreatedLight() {
        val a = getString(R.string.event_finish_time)
        binding.button.setOnClickListener {
            try {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            } catch (e: Exception) {
                Log.e("PhotoPicker", "PHOTOPICKER FAILED!", e)
                nancyToastError(
                    requireContext(),
                    getString(R.string.something_went_wrong_colon) + e.message,
                    NancyToast.LENGTH_LONG
                )
            }
        }
        binding.buttonGoShimmer.setOnClickListener {
            findNavController().navigate(R.id.action_test1Fragment_to_shimmerFragment)
        }
    }

    private fun uploadImageToApi(uri: Uri) {
        val context = requireContext()
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "temp_image").apply {
            outputStream().use { inputStream?.copyTo(it) }
        }

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestBody)

        lifecycleScope.launch {
            try {
                val response = api.uploadFileAndGetLink("events", multipartBody)
                handleApiResponse(response)
            } catch (e: Exception) {
                Log.e("UploadError", "Failed to upload image", e)
                nancyToastError(requireContext(), getString(R.string.upload_failed_colon) + e.message)
            }
        }
    }

    private fun handleApiResponse(response: Response<FileUploadGetLinkResponse>) {
        if (response.isSuccessful) {
            val link = response.body()?.link ?: "No link provided"
            nancyToastSuccess(requireContext(), getString(R.string.image_upload_successful_colon) + link)

            // Update the image view with the uploaded image
            Glide.with(binding.imageView)
                .load(link)
                .placeholder(com.example.common.R.drawable.ic_warning)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageView)
        } else {
            nancyToastError(requireContext(), getString(R.string.upload_failed_colon) + response.errorBody()?.string())
        }
    }
}
