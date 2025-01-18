package com.example.eventify.presentation.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.common.base.BaseActivity
import com.example.common.utils.AppUtils
import com.example.common.utils.RequestChannel
import com.example.data.remote.interceptor.TokenManager
import com.example.eventify.EventifyApplication
import com.example.eventify.NetworkUtils
import com.example.eventify.NetworkUtils.initializeTokenManager
import com.example.eventify.R
import com.example.eventify.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateLight() {

        val app = application as EventifyApplication
        app.isNetworkConnected.observe(this) { isConnected ->
            if (!isConnected)
                navigateToNetworkActivity()

        }

        //disabling bottom navigation hint
        binding.bottomNavigationView.menu.forEach {
            val menuItem = binding.bottomNavigationView.findViewById<View>(it.itemId)
            menuItem.setOnLongClickListener {
                true
            }
        }

        setUpBottomNavigation()
        initializeTokenManager()
    }

    private fun navigateToNetworkActivity() {
        Intent(this@MainActivity, NetworkLostActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun initializeTokenManager() {
        initializeTokenManager(tokenManager)

        lifecycleScope.launch {
            try {
                AppUtils.authChannel.receiveAsFlow().collectLatest { request ->
                    if(request == RequestChannel.ON_401_ERROR){
                        Snackbar.make(binding.root, getString(R.string.error_request), Snackbar.LENGTH_SHORT).show()
                    }

                    if (request == RequestChannel.LOG_OUT) {
                        NetworkUtils.handleLogout(this@MainActivity)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    @SuppressLint("RestrictedApi")
    private fun setUpBottomNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerViewMain) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView, navHostFragment.navController
        )
    }
}