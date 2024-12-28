package com.example.eventify.presentation.ui.fragments.places

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.common.base.BaseFragment
import com.example.eventify.databinding.FragmentEventsBinding

class PlacesViewPagerAdapter(
    list: ArrayList<BaseFragment<FragmentEventsBinding>>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    private val fragmentList = list

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}