package com.example.common.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object RuntimeLocaleChanger {

    fun wrapContext(context: Context): Context {

        val savedLocale = createLocaleFromSavedLanguage(context) // load the user picked language from persistence (e.g SharedPreferences)
            ?: return context // else return the original untouched context

        // as part of creating a new context that contains the new locale we also need to override the default locale.
        Locale.setDefault(savedLocale)

        // create new configuration with the saved locale
        val newConfig = Configuration()
        newConfig.setLocale(savedLocale)

        return context.createConfigurationContext(newConfig)
    }

    private fun createLocaleFromSavedLanguage(context: Context): Locale? {
        val sharedPref = context.getSharedPreferences("language", Context.MODE_PRIVATE)
        val languageCode = sharedPref.getString("language", "en")
        return if (languageCode!!.isNotEmpty()) Locale(languageCode) else null
    }
}