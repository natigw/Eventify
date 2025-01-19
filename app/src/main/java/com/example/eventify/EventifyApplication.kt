package com.example.eventify

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
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
import java.util.Locale
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidApp
class EventifyApplication : Application() {

    @Inject
    lateinit var networkConnection: NetworkConnection

    @Inject
    lateinit var tokenManager: TokenManager

    @Inject
    @Named("LanguageChoice")
    lateinit var sharedPrefLanguage : SharedPreferences

    @Inject
    @Named("ThemeChoice")
    lateinit var sharedPrefTheme : SharedPreferences

    private val _isNetworkConnected = MutableLiveData<Boolean>()
    val isNetworkConnected: LiveData<Boolean> get() = _isNetworkConnected

    override fun onCreate() {
        super.onCreate()

        val languageCode = sharedPrefLanguage.getString("language", "en") ?: "en"
        setAppLocale(this, languageCode)
        val themeMode = sharedPrefTheme.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        AppCompatDelegate.setDefaultNightMode(themeMode)

        observeNetworkConnection()
    }

    private fun setAppLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    private fun observeNetworkConnection() {
        networkConnection.isConnected.observeForever { isConnected ->
            _isNetworkConnected.postValue(isConnected)
        }
    }
}