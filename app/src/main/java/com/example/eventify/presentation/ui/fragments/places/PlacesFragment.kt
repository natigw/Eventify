package com.example.eventify.presentation.ui.fragments.places

import androidx.lifecycle.lifecycleScope
import com.example.eventify.R
import com.example.common.base.BaseFragment
import com.example.data.remote.api.VenueAPI
import com.example.data.remote.interceptor.TokenManager
import com.example.domain.repository.AuthRepository
import com.example.eventify.databinding.FragmentPlacesBinding
import com.example.eventify.presentation.ui.fragments.places.event.EventsFragment
import com.example.eventify.presentation.ui.fragments.places.venue.VenuesFragment
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlacesFragment : BaseFragment<FragmentPlacesBinding>(FragmentPlacesBinding::inflate) {


    @Inject
    lateinit var venueAPI: VenueAPI

    @Inject
    lateinit var authAPI: AuthRepository

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onViewCreatedLight() {
        setTabLayoutMediator()

        binding.button5.setOnClickListener {
            lifecycleScope.launch {
                try {
                    authAPI.verifyUserToken("")
                }
                catch (e : Exception) {

                }


            }

        }

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