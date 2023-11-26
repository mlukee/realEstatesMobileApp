package com.example.poraapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.poraapplication.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private lateinit var binding1: ActivityMainBinding

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

        if(intent.hasExtra("AREA") && intent.hasExtra("PRICE") && intent.hasExtra("PROPERTY_TYPE")){
            val property = intent.getStringExtra("PROPERTY_TYPE")
            val area = intent.getDoubleExtra("AREA", 0.0)
            val price = intent.getDoubleExtra("PRICE", 0.0)
            binding1.inputPropertyType?.editText?.setText(property)
            binding1.inputArea?.editText?.setText(area.toString())
            binding1.inputPrice?.editText?.setText(price.toString())
        }

    }

    fun onButtonAddClick(view: View) {
        val property = binding1.inputPropertyType?.editText?.text.toString()
        val etArea = binding1.inputArea?.editText?.text.toString()
        val etPrice = binding1.inputPrice?.editText?.text.toString()
        var position = 0;
        if(intent.hasExtra("POSITION")){
            position = intent.getIntExtra("POSITION", 0)
        }
        try {
            if (property.isEmpty() || etArea.isEmpty() || etPrice.isEmpty()) {
                throw IllegalArgumentException("All fields are required")
            }

            val area = etArea.toDouble()
            val price = etPrice.toDouble()

            val intent = Intent(this, MainActivity1::class.java)
            intent.putExtra(InputTypes.PROPERTY_TYPE.name, property)
            intent.putExtra(InputTypes.AREA.name, area)
            intent.putExtra(InputTypes.PRICE.name, price)
            intent.putExtra("POSITION", position)

            binding1.inputPropertyType?.editText?.text?.clear()
            binding1.inputArea?.editText?.text?.clear()
            binding1.inputPrice?.editText?.text?.clear()

            setResult(RESULT_OK, intent)
            finish()


        } catch (e: IllegalArgumentException) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()

        }

    }
}