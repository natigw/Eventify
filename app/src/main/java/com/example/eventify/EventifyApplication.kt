package com.example.eventify

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.common.utils.AppUtils
import com.example.common.utils.NetworkConnection
import com.example.common.utils.RequestChannel
import com.example.data.remote.interceptor.TokenManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltAndroidApp
class EventifyApplication : Application() {

    @Inject
    lateinit var networkConnection: NetworkConnection

    @Inject
    lateinit var tokenManager: TokenManager

    private val _isNetworkConnected = MutableLiveData<Boolean>()
    val isNetworkConnected: LiveData<Boolean> get() = _isNetworkConnected

    // Manually define applicationScope for the lifecycle of the Application
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        // Observe network changes
        observeNetworkConnection()

        // Initialize token manager and auth channel
        initializeTokenManager()
    }

    private fun observeNetworkConnection() {
        networkConnection.isConnected.observeForever { isConnected ->
            _isNetworkConnected.postValue(isConnected)
        }
    }

    private fun initializeTokenManager() {
        // Token manager initialization
        NetworkUtils.initializeTokenManager(tokenManager)

        // Launch a coroutine for observing auth channel safely
        applicationScope.launch {
            try {
                AppUtils.authChannel.receiveAsFlow().collectLatest { request ->
                    if (request == RequestChannel.LOG_OUT) {
                        NetworkUtils.handleLogout(this@EventifyApplication)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        applicationScope.cancel() // Cancel coroutines when the app is terminated
    }
}