package com.example.poraapplication

import android.R
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import com.example.poraapplication.databinding.ActivityMain1Binding
import com.example.poraapplication.databinding.ActivitySettingsBinding
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    companion object {
        private const val KEY_ACTIVITY_OPENS_PREFIX = "activity_opens_"

        fun incrementActivityOpenCount(activity: AppCompatActivity) {
            val sharedPreferences = activity.getSharedPreferences(MyApplication.PREFS_NAME, MODE_PRIVATE)
            val className = activity.localClassName
            val currentCount = sharedPreferences.getInt(KEY_ACTIVITY_OPENS_PREFIX + className, 0)
            sharedPreferences.edit().putInt(KEY_ACTIVITY_OPENS_PREFIX + className, currentCount + 1).apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        incrementActivityOpenCount(this)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the switch state from SharedPreferences
        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        binding.switchTheme.isChecked = sharedPreferences.getBoolean("theme", false)

        val languages = listOf("English", "Slovenian", "Deutsch")
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, languages)
        binding.spinnerLanguage.adapter = adapter
    }


    fun onSetThemeButtonClick(view: View) {
        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val isNightMode = sharedPreferences.getBoolean("theme", false)
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.switchTheme.isChecked = false
            sharedPreferences.edit().putBoolean("theme", false).apply()
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.switchTheme.isChecked = true
            sharedPreferences.edit().putBoolean("theme", true).apply()
        }
    }
}