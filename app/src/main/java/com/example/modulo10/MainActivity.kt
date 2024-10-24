package com.example.modulo10

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        // Carregar o idioma persistente salvo nas preferências
        loadLocale()
        setContentView(R.layout.activity_main)

        val radioGroupLanguage = findViewById<RadioGroup>(R.id.radioGroupLanguage)

        // Restaurar o estado dos RadioButtons
        restoreCheckedLanguage()

        // Listener para mudar o idioma
        radioGroupLanguage.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioEnglish -> setLocale("en")
                R.id.radioSpanish -> setLocale("es")
                R.id.radioPortuguese -> setLocale("pt")
            }
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

            // Recria a Activity somente se o idioma foi alterado
            recreate()
        }
    }

    private fun loadLocale() {
        val languageCode = sharedPreferences.getString("App_Language", "pt") ?: "pt"
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }

    // Função para restaurar o estado dos RadioButtons
    private fun restoreCheckedLanguage() {
        val currentLanguage = sharedPreferences.getString("App_Language", "pt")
        val radioGroupLanguage = findViewById<RadioGroup>(R.id.radioGroupLanguage)

        // Verifica qual idioma está salvo e seleciona o RadioButton correspondente
        when (currentLanguage) {
            "en" -> radioGroupLanguage.check(R.id.radioEnglish)
            "es" -> radioGroupLanguage.check(R.id.radioSpanish)
            "pt" -> radioGroupLanguage.check(R.id.radioPortuguese)
        }
    }
}
