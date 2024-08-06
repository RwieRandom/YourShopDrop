package de.your.yourshopdrop

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import java.util.Locale

class LanguageManager(private val activity: MainActivity) {

    //TODO: Französisch, Spanisch
    private val languages = activity.resources.getStringArray(R.array.language)
    private val languageKey = "language"
    private val prefs : SharedPreferences = activity.getSharedPreferences(languageKey, Context.MODE_PRIVATE)

    fun setupSpinnerListener(languageSpinner: Spinner, screenManager: ScreenManager){
        // Set up the spinner with an adapter
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        // Set the spinner to the current language
        val currentLanguage = getCurrentLanguage()
        val currentPosition = languages.indexOf(currentLanguage)
        if (currentPosition >= 0) {
            languageSpinner.setSelection(currentPosition)
        }

        // Set up the onItemSelectedListener
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedLanguage = parent.getItemAtPosition(position) as String
                if (selectedLanguage != currentLanguage) {
                    changeLanguage(selectedLanguage)
                    screenManager.hideScreen()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    //TODO: Dass ich nicht alles mehrmals schreiben muss diese de -> Deutsch usw
    private fun getCurrentLanguage(): String {
        val savedLanguage = prefs.getString(languageKey, null)
        if (savedLanguage != null) {
            return savedLanguage
        }
        val locale = Locale.getDefault()
        return when (locale.language) {
            "de" -> "Deutsch"
            "en" -> "English"
            else -> "English"
        }
    }

    private fun changeLanguage(language: String) {
        with(prefs.edit()) {
            putString(languageKey, language)
            apply()
        }

        val locale = when (language) {
            "Deutsch" -> Locale("de")
            "English" -> Locale("en")
            else -> Locale("en") // Fallback language
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)

        activity.recreate()
    }

    fun applySavedLanguageOnStart() {
        val savedLanguage = getCurrentLanguage()
        val locale = when (savedLanguage) {
            "Deutsch" -> Locale("de")
            "English" -> Locale("en")
            else -> Locale("en") // Fallback language
        }
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
    }

}