package com.example.poraapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.example.poraapplication.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        incrementActivityOpenCount(this)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSharedPreferences()
        setupNotifications()
    }

    private fun setupSharedPreferences() {
        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        binding.switchTheme.isChecked = sharedPreferences.getBoolean("theme", false)
    }

    private fun setupNotifications() {
        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        binding.switchNotifications.isChecked = sharedPreferences.getBoolean("notifications", true)

        // Listener for notification switch changes
        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            with(sharedPreferences.edit()) {
                putBoolean("notifications", isChecked)
                apply()
            }
        }
    }

    fun onSetThemeButtonClick(view:View){
        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val theme = sharedPreferences.getBoolean("theme", false)
        val mode = if(theme) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
        if(AppCompatDelegate.getDefaultNightMode() != mode){
            AppCompatDelegate.setDefaultNightMode(mode)
            with(sharedPreferences.edit()) {
                putBoolean("theme", !theme)
                apply()
            }
        }
    }

    companion object {
        private const val KEY_ACTIVITY_OPENS_PREFIX = "activity_opens_"

        fun incrementActivityOpenCount(activity: AppCompatActivity) {
            val sharedPreferences = activity.getSharedPreferences(MyApplication.PREFS_NAME, MODE_PRIVATE)
            val className = activity.localClassName
            val currentCount = sharedPreferences.getInt(KEY_ACTIVITY_OPENS_PREFIX + className, 0)
            sharedPreferences.edit().putInt(KEY_ACTIVITY_OPENS_PREFIX + className, currentCount + 1).apply()
        }
    }
}