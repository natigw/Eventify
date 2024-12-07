package com.example.eventify

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eventify.common.utils.NetworkConnection
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class EventifyApplication : Application() {

    @Inject
    lateinit var networkConnection: NetworkConnection

    private val _isNetworkConnected = MutableLiveData<Boolean>()
    val isNetworkConnected: LiveData<Boolean> get() = _isNetworkConnected

    override fun onCreate() {
        super.onCreate()

        // Observe network changes
        networkConnection.isConnected.observeForever { isConnected ->
            _isNetworkConnected.postValue(isConnected)
        }
    }
}