package com.example.eventify.presentation.ui.fragments.events

import com.example.eventify.R
import com.example.common.base.BaseFragment
import com.example.data.remote.api.VenueAPI
import com.example.data.remote.interceptor.TokenManager
import com.example.domain.repository.AuthRepository
import com.example.eventify.databinding.FragmentPlacesBinding
import com.example.eventify.presentation.ui.fragments.events.customEvent.CustomEventsFragment
import com.example.eventify.presentation.ui.fragments.events.event.EventsFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PlacesFragment : BaseFragment<FragmentPlacesBinding>(FragmentPlacesBinding::inflate) {

    override fun onViewCreatedLight() {
        setTabLayoutMediator()
    }

    private fun setTabLayoutMediator(){
        val tabTitles = listOf(getString(R.string.organizer_events),getString(R.string.custom_events))

        val fragmentList = arrayListOf(
            EventsFragment(),
            CustomEventsFragment()
        )
        val placesViewPagerAdapter = PlacesViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager,lifecycle)
        binding.viewPagerPlaces.adapter = placesViewPagerAdapter
        TabLayoutMediator(binding.tabLayout,binding.viewPagerPlaces) {tab,position->
            tab.text = tabTitles[position]
        }.attach()
    }

}