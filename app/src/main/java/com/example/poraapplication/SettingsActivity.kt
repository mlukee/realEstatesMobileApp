package com.example.poraapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.example.poraapplication.databinding.ActivityMain1Binding
import com.example.poraapplication.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the switch state from SharedPreferences
        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        binding.switchTheme.isChecked = sharedPreferences.getBoolean("theme", false)

    }

    fun onSetThemeButtonClick(view: View){
        val sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE)
        val isNightMode = sharedPreferences.getBoolean("theme", false)
        if(isNightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.switchTheme.isChecked = false
            sharedPreferences.edit().putBoolean("theme", false).apply()
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.switchTheme.isChecked = true
            sharedPreferences.edit().putBoolean("theme", true).apply()
        }
    }
}