package com.example.krishisangam

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

data class AppLanguage(
    val name: String,
    val nativeName: String,
    val languageTag: String
)

object LanguageManager {

    private const val PREF_NAME = "krishi_sangam_language_pref"
    private const val KEY_LANGUAGE = "selected_language"

    val supportedLanguages = listOf(
        AppLanguage("English", "English", "en"),
        AppLanguage("Hindi", "हिन्दी", "hi"),
        AppLanguage("Marathi", "मराठी", "mr"),
        AppLanguage("Gujarati", "ગુજરાતી", "gu"),
        AppLanguage("Punjabi", "ਪੰਜਾਬੀ", "pa"),
        AppLanguage("Bengali", "বাংলা", "bn"),
        AppLanguage("Tamil", "தமிழ்", "ta"),
        AppLanguage("Telugu", "తెలుగు", "te"),
        AppLanguage("Kannada", "ಕನ್ನಡ", "kn"),
        AppLanguage("Malayalam", "മലയാളം", "ml")
    )

    fun saveLanguage(
        context: Context,
        languageTag: String
    ) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, languageTag)
            .apply()
    }

    fun getSavedLanguage(
        context: Context
    ): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, "en") ?: "en"
    }

    fun applyLanguage(
        languageTag: String
    ) {
        val localeList = LocaleListCompat.forLanguageTags(languageTag)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    fun changeLanguage(
        context: Context,
        languageTag: String
    ) {
        saveLanguage(context, languageTag)
        applyLanguage(languageTag)

        context.findActivity()?.recreate()
    }
}

fun Context.findActivity(): Activity? {
    var currentContext = this

    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }

        currentContext = currentContext.baseContext
    }

    return null
}