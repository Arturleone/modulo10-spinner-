package com.example.modulo10

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var spinnerLanguage: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

        // Carregar o idioma persistente salvo nas preferências
        loadLocale()

        setContentView(R.layout.activity_main)

        spinnerLanguage = findViewById(R.id.spinnerLanguage)

        // Criar um ArrayAdapter para o Spinner com strings localizadas
        val languages = arrayOf(
            getString(R.string.language_portuguese),
            getString(R.string.language_english),
            getString(R.string.language_spanish)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = adapter

        // Definir a posição do Spinner com base na preferência salva
        val savedLanguageCode = sharedPreferences.getString("App_Language", null) ?: "pt"
        spinnerLanguage.setSelection(getLanguagePosition(savedLanguageCode))

        // Listener para mudar o idioma
        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val languageCode = arrayOf("pt", "en", "es")[position]
                setLocale(languageCode)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Não faz nada
            }
        }
    }

    private fun getLanguagePosition(languageCode: String): Int {
        return when (languageCode) {
            "pt" -> 0
            "en" -> 1
            "es" -> 2
            else -> 0 // Padrão para português
        }
    }

    private fun setLocale(languageCode: String) {
        val currentLocale = sharedPreferences.getString("App_Language", "pt")

        // Verifica se o idioma já está configurado para evitar recriações desnecessárias
        if (currentLocale != languageCode) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            val config = resources.configuration
            config.setLocale(locale)

            resources.updateConfiguration(config, resources.displayMetrics)

            // Salvar o idioma selecionado nas preferências para persistência
            sharedPreferences.edit().putString("App_Language", languageCode).apply()

            // Recarregar o Spinner com as traduções corretas
            reloadSpinner()

            // Recria a Activity somente se o idioma foi alterado
            recreate()
        }
    }

    private fun reloadSpinner() {
        val languages = arrayOf(
            getString(R.string.language_portuguese),
            getString(R.string.language_english),
            getString(R.string.language_spanish)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = adapter
        // Redefine a seleção do Spinner com base no idioma salvo
        val savedLanguageCode = sharedPreferences.getString("App_Language", "pt") ?: "pt"
        spinnerLanguage.setSelection(getLanguagePosition(savedLanguageCode))
    }

    private fun loadLocale() {
        val languageCode = sharedPreferences.getString("App_Language", "pt") ?: "pt"
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
