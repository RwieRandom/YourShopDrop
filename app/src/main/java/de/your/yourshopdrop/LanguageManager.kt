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

    private val languages = activity.resources.getStringArray(R.array.language)
    private val languageKey = "language"
    private val prefs : SharedPreferences = activity.getSharedPreferences(languageKey, Context.MODE_PRIVATE)

    fun setupSpinnerListener(languageSpinner: Spinner, screenManager: ScreenManager) {
        // Sortiere die Sprachen nach Anfangsbuchstaben
        val sortedLanguages = languages.sorted()

        // Set up the spinner with an adapter
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, sortedLanguages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        // Set the spinner to the current language
        val currentLanguage = getCurrentLanguage()
        val currentPosition = sortedLanguages.indexOf(currentLanguage)
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

    // Mapping von Sprach-Strings zu Locale-Objekten
    private val languageMap = mapOf(
        "Deutsch" to Locale("de"),
        "English" to Locale("en"),
        "Français" to Locale("fr"),
        "Español" to Locale("es"),
        "Italiano" to Locale("it"),
    )

    private fun getCurrentLanguage(): String {
        val savedLanguage = prefs.getString(languageKey, null)
        if (savedLanguage != null && languageMap.containsKey(savedLanguage)) {
            return savedLanguage
        }

        val locale = Locale.getDefault()
        return languageMap.entries.find { it.value.language == locale.language }?.key ?: "English"
    }

    private fun changeLanguage(language: String) {
        with(prefs.edit()) {
            putString(languageKey, language)
            apply()
        }

        val locale = languageMap[language] ?: Locale("en") // Fallback language
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)

        activity.recreate()
    }

    fun applySavedLanguageOnStart() {
        val savedLanguage = getCurrentLanguage()
        val locale = languageMap[savedLanguage] ?: Locale("en") // Fallback language
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)

        // Wenn du API 24 oder höher anvisierst
        activity.createConfigurationContext(config)

        // Wenn du den Kontext nicht ändern willst, aber Ressourcen neu laden möchtest
        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)

    }

}