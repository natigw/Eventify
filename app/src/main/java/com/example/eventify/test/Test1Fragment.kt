package com.example.eventify.test

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color.alpha
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat.animate
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.common.base.BaseFragment
import com.example.common.utils.NancyToast
import com.example.common.utils.crossfadeAppear
import com.example.common.utils.crossfadeDisappear
import com.example.common.utils.nancyToastError
import com.example.common.utils.nancyToastSuccess
import com.example.data.remote.api.EventAPI
import com.example.data.remote.model.events.FileUploadGetLinkResponse
import com.example.eventify.R
import com.example.eventify.databinding.FragmentTest1Binding
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.lang.reflect.Type
import javax.inject.Inject

@AndroidEntryPoint
class Test1Fragment : BaseFragment<FragmentTest1Binding>(FragmentTest1Binding::inflate) {

    @Inject
    lateinit var api: EventAPI



    override fun onViewCreatedLight() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(2000)
            crossfadeAppear(binding.imageView5, 3000)
            delay(2000)
            crossfadeDisappear(binding.progressBar)
        }


        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            openGallery()
        }

        binding.button.setOnClickListener {
            openGallery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            imageUri?.let {
                uploadImage(it)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun uploadImage(uri: Uri) {
        val filePart = prepareFilePart(uri)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = api.uploadFileAndGetLink("events", filePart)
                handleApiResponse(response)
            } catch (e: Exception) {
                Log.e("UploadError", "Failed to upload image", e)
                nancyToastError(
                    requireContext(),
                    getString(R.string.upload_failed_colon) + e.message
                )
            }
        }
    }

    private fun prepareFilePart(uri: Uri): MultipartBody.Part {
        val file = File(getRealPathFromURI(uri))
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    private fun getRealPathFromURI(uri: Uri): String {
        var path = ""
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context?.contentResolver?.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            path = it.getString(columnIndex)
        }
        return path
    }

    private fun handleApiResponse(response: Response<FileUploadGetLinkResponse>) {
        if (response.isSuccessful) {
            val bodyString = response.body()?.link ?: "No link provided"
            nancyToastSuccess(
                requireContext(),
                getString(R.string.image_upload_successful_colon) + bodyString
            )
            Glide.with(binding.imageView)
                .load(bodyString)
                .placeholder(com.example.common.R.drawable.ic_warning)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.imageView)
        } else {
            val errorBody = response.errorBody()?.string() ?: "Unknown error"
            Log.e("API Error", errorBody)
            nancyToastError(
                requireContext(),
                getString(R.string.upload_failed_colon) + errorBody
            )
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 888
        private const val GALLERY_REQUEST_CODE = 999
    }
}

class FileUploadGetLinkResponseDeserializer : JsonDeserializer<FileUploadGetLinkResponse> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): FileUploadGetLinkResponse {
        return if (json.isJsonObject) {
            val jsonObject = json.asJsonObject
            val link = jsonObject.get("link")?.asString ?: ""
            FileUploadGetLinkResponse(link)
        } else {
            FileUploadGetLinkResponse(json.asString)
        }
    }
}

val gson = GsonBuilder()
    .registerTypeAdapter(
        FileUploadGetLinkResponse::class.java,
        FileUploadGetLinkResponseDeserializer()
    )
    .create()

val retrofit = Retrofit.Builder()
    .baseUrl("https://eventify-az.onrender.com/")
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()
