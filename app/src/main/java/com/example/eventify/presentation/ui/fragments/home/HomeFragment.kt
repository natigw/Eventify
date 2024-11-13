package com.example.eventify.presentation.ui.fragments.home

import androidx.lifecycle.lifecycleScope
import com.example.eventify.common.base.BaseFragment
import com.example.eventify.data.remote.api.EventifyAPI
import com.example.eventify.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    @Inject
    lateinit var api: EventifyAPI

    override fun onViewCreatedLight() {
        lifecycleScope.launch {
            val response = api.getAllVenues()
            binding.textView2.text = response.toString()
        }
    }

    override fun observeChanges() {

    }
}