package com.example.test

import androidx.lifecycle.lifecycleScope
import com.example.common.base.BaseFragment
import com.example.data.remote.api.VenueAPI
import com.example.test.databinding.FragmentTest1Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Test1Fragment : BaseFragment<FragmentTest1Binding>(FragmentTest1Binding::inflate) {

    @Inject
    lateinit var api: VenueAPI

    override fun onViewCreatedLight() {
        lifecycleScope.launch {
//            api.createVenue(
//                RequestCreateVenue(
//                    description = "yeni mekan",
//                    lat = "40.3360106",
//                    lng = "49.8363917",
//                    name = "Suraxanı Gəmi Muzeyi",
//                    venueType = "museum",
//                    workHoursOpen = "10:00",
//                    workHoursClose = "17:002"
//                )
//            )

            binding.tvTest.text = api.searchVenue("banM").body().toString()

        }
    }
}