package com.example.eventify.presentation.ui.activities

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.eventify.R
import com.example.eventify.common.base.BaseActivity
import com.example.eventify.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    override fun onCreateLight() {

        setUpBottomNavigation()

    }

    private fun setUpBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerViewMain) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView, navHostFragment.navController
        )
    }
}