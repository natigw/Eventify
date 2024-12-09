package com.example.eventify.test

import androidx.lifecycle.lifecycleScope
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.data.remote.api.VenueAPI
import com.example.eventify.data.remote.model.venues.comment.addComment.RequestAddVenueComment
import com.example.eventify.databinding.FragmentTest1Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Test1Fragment : BaseFragment<FragmentTest1Binding>(FragmentTest1Binding::inflate) {

    @Inject
    lateinit var api: VenueAPI

    override fun onViewCreatedLight() {
        lifecycleScope.launch {
            api.addVenueComment(
                token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJibHVlc3RlZWwyIiwiaWQiOjM0LCJleHAiOjE3MzM3NjAzNjN9.yA4sWGPmJ4LuqspxkmUrTgyxJslNvM9Wg2f4RA82vV8",
                RequestAddVenueComment(
                    content = "sadfjsbjkfbasl",
                    venue = 5
                )
            )
        }
    }
}