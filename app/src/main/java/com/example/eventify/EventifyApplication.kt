package com.example.eventify

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.common.utils.NetworkConnection
import com.example.data.remote.interceptor.TokenManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidApp
class EventifyApplication : Application() {

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    @Named("ThemeChoice")
    lateinit var sharedPrefTheme : SharedPreferences

    @Inject
    lateinit var networkConnection: NetworkConnection

    private val _isNetworkConnected = MutableLiveData<Boolean>()
    val isNetworkConnected: LiveData<Boolean> get() = _isNetworkConnected

    override fun onCreate() {
        super.onCreate()

        val themeMode = sharedPrefTheme.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(themeMode)

        observeNetworkConnection()
    }

    private fun observeNetworkConnection() {
        networkConnection.isConnected.observeForever { isConnected ->
            _isNetworkConnected.postValue(isConnected)
        }
    }
}