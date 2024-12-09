package com.example.eventify.test

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.common.utils.NancyToast
import com.example.eventify.data.remote.api.VenueAPI
import com.example.eventify.data.remote.model.venues.comment.addComment.RequestAddCommentVenue
import com.example.eventify.databinding.FragmentTest1Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Test1Fragment : BaseFragment<FragmentTest1Binding>(FragmentTest1Binding::inflate) {

    @Inject
    lateinit var api: VenueAPI

    override fun onViewCreatedLight() {

//        lifecycleScope.launch {
//
//            val data = api.getVenueDetails(12)
//
//            if (data.isSuccessful) {
//                binding.tvTest.text = data.body()?.name
//            }
//            else {
//                binding.tvTest.text = data.errorBody()?.string()
//            }
//            Log.e("network", data.message())
//
//
//        }

        lifecycleScope.launch {
            api.addCommentVenue(
                token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJibHVlc3RlZWwyIiwiaWQiOjM0LCJleHAiOjE3MzM3NjAzNjN9.yA4sWGPmJ4LuqspxkmUrTgyxJslNvM9Wg2f4RA82vV8",
                RequestAddCommentVenue(
                    content = "sadfjsbjkfbasl",
                    venue = 5
                )
            )

            binding.tvTest.text = api.getVenueComments(5).body().toString()

        }
    }

}