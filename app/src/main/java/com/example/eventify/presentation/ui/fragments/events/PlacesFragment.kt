package com.example.eventify.presentation.ui.fragments.events

import com.example.eventify.R
import com.example.common.base.BaseFragment
import com.example.eventify.databinding.FragmentPlacesBinding
import com.example.eventify.presentation.ui.fragments.events.customEvent.UserEventsFragment
import com.example.eventify.presentation.ui.fragments.events.event.EventsFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacesFragment : BaseFragment<FragmentPlacesBinding>(FragmentPlacesBinding::inflate) {

    override fun onViewCreatedLight() {
        setTabLayoutMediator()
    }

    private fun setTabLayoutMediator(){
        val tabTitles = listOf(getString(R.string.organizer_events),getString(R.string.custom_events))

        val fragmentList = arrayListOf(
            EventsFragment(),
            UserEventsFragment()
        )
        val placesViewPagerAdapter = PlacesViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager,lifecycle)
        binding.viewPagerPlaces.adapter = placesViewPagerAdapter
        TabLayoutMediator(binding.tabLayout,binding.viewPagerPlaces) {tab,position->
            tab.text = tabTitles[position]
        }.attach()
    }
}