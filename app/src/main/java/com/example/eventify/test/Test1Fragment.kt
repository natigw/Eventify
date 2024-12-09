package com.example.eventify.test

import android.util.Log
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
                RequestAddVenueComment(
                    content = "sadfjsbjkfbasl",
                    venue = 5
                )
            )

        }
    }
}