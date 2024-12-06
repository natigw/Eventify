package com.example.eventify.presentation.ui.activities

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.eventify.R
import com.example.eventify.common.base.BaseActivity
import com.example.eventify.common.utils.AppUtils
import com.example.eventify.data.remote.interceptor.TokenManager
import com.example.eventify.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {


    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateLight() {

        setUpBottomNavigation()

        lifecycleScope.launch {
            AppUtils.authChannel.receiveAsFlow().collectLatest {
                AppUtils.handleLogout(this@MainActivity,tokenManager)
            }
        }


    }

    @SuppressLint("RestrictedApi")
    private fun setUpBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerViewMain) as NavHostFragment
        lifecycleScope.launch {
//            navHostFragment.navController.currentBackStackEntryFlow.collectLatest {
//                Log.e("stack", it.toString())
//            }
            navHostFragment.navController.currentBackStack.collectLatest {
                Log.e("stackSize", it.size.toString())
                it.forEach { salam ->
                    Log.e("stack", salam.toString())
                }
            }
        }
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView, navHostFragment.navController
        )
    }
}