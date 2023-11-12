package com.example.poraapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.poraapplication.databinding.ActivityMain1Binding
import com.example.poraapplication.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}