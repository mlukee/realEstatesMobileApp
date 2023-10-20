package com.example.poraapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.poraapplication.databinding.ActivityAboutBinding
import com.example.poraapplication.databinding.ActivityMain1Binding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}