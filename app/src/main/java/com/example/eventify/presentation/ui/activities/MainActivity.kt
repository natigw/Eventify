package com.example.eventify.presentation.ui.activities

import android.annotation.SuppressLint
import android.util.Log
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

        // Access the application instance
        val app = application as EventifyApplication

        // Observe global network state
        app.isNetworkConnected.observe(this) { isConnected ->
            if (!isConnected) {
//                Snackbar.make(
//                    findViewById(android.R.id.content),
//                    "Connection Lost",
//                    Snackbar.LENGTH_INDEFINITE
//                ).apply {
//                    setAction("Dismiss") { dismiss() }
//                }.show()
            }
//            else (isConnected)
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
    private fun initializeTokenManager() {
        NetworkUtils.initializeTokenManager(tokenManager)

        lifecycleScope.launch {
            try {
                AppUtils.authChannel.receiveAsFlow().collectLatest { request ->
                    if(request == RequestChannel.ON_401_ERROR){
                        Snackbar.make(binding.root, getString(R.string.retry_snack), Snackbar.LENGTH_SHORT).show()
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
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerViewMain) as NavHostFragment
        lifecycleScope.launch {
//            navHostFragment.navController.currentBackStackEntryFlow.collectLatest {
//                Log.e("stack", it.toString())
//            }
            navHostFragment.navController.currentBackStack.collectLatest {
//                Log.e("stackSize", it.size.toString())
//                it.forEach { salam ->
//                    Log.e("stack", salam.toString())
//                }
            }
        }
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView, navHostFragment.navController
        )
    }
}