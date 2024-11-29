package com.example.eventify.presentation.ui.fragments.places

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.eventify.R
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.databinding.FragmentPlacesBinding
import com.example.eventify.presentation.ui.fragments.onBoarding.welcome.FirstFragment
import com.example.eventify.presentation.ui.fragments.onBoarding.welcome.SecondFragment
import com.example.eventify.presentation.ui.fragments.onBoarding.welcome.ThirdFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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
        binding.viewPager.adapter = placesViewPagerAdapter
        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,position->
            tab.text = tabTitles[position]
        }.attach()
    }

}