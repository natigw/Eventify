package com.example.eventify.presentation.ui.fragments.places

import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.databinding.FragmentPlacesBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlacesFragment : BaseFragment<FragmentPlacesBinding>(FragmentPlacesBinding::inflate) {

    override fun onViewCreatedLight() {
        setTabLayoutMediator()
    }

    private fun setTabLayoutMediator(){
        val tabTitles = listOf(getString(R.string.venues),getString(R.string.events))

        val fragmentList = arrayListOf(
            VenuesFragment(),
            EventsFragment()
        )
        val placesViewPagerAdapter = PlacesViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager,lifecycle)
        binding.viewPagerPlaces.adapter = placesViewPagerAdapter
        TabLayoutMediator(binding.tabLayout,binding.viewPagerPlaces) {tab,position->
            tab.text = tabTitles[position]
        }.attach()
    }

}