package com.example.poraapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.poraapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onButttonInfoClick(view: View){
        Log.d(MainActivity::class.simpleName, 5.toString())
        //MainActivity::class.simpleName lahko zamenjas z svojo znacko da lahko lazje isces v Logcat
    }
}