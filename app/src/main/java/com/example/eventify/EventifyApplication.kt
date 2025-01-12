package com.example.eventify

import android.app.Application
import android.util.Log
import android.widget.Toast
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

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _isNetworkConnected = MutableLiveData<Boolean>()
    val isNetworkConnected: LiveData<Boolean> get() = _isNetworkConnected

    override fun onCreate() {
        super.onCreate()

        // Observe network changes
        observeNetworkConnection()

    }

    private fun observeNetworkConnection() {
        networkConnection.isConnected.observeForever { isConnected ->
            _isNetworkConnected.postValue(isConnected)
        }
    }



    override fun onTerminate() {
        super.onTerminate()
    }
}