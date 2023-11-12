package com.example.poraapplication

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.lib.RealEstate
import com.example.poraapplication.databinding.ActivityMainBinding
import kotlinx.serialization.json.Json
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding1: ActivityMainBinding

    private var qrCodeData: String? = null
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
        binding1 = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding1.root)
    }

    fun onButtonAddClick(view: View) {
        val property = binding1.inputPropertyType?.editText?.text.toString()
        val area = binding1.inputArea?.editText?.text.toString()
        val price = binding1.inputPrice?.editText?.text.toString()
        try {
            if (property.isEmpty() || area.isEmpty() || price.isEmpty()) {
                throw IllegalArgumentException("All fields are required")
            }

            val area = area.toDouble()
            val price = price.toDouble()

            val intent = Intent(this, MainActivity1::class.java)
            intent.putExtra(InputTypes.PROPERTY_TYPE.name, property)
            intent.putExtra(InputTypes.AREA.name, area)
            intent.putExtra(InputTypes.PRICE.name, price)

            binding1.inputPropertyType?.editText?.text?.clear()
            binding1.inputArea?.editText?.text?.clear()
            binding1.inputPrice?.editText?.text?.clear()

            setResult(RESULT_OK, intent)
            finish()


        } catch (e: IllegalArgumentException) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show();

        }

    }
}