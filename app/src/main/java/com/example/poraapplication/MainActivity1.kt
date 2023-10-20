package com.example.poraapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.lib.RealEstate
import com.example.poraapplication.databinding.ActivityMain1Binding
import io.github.serpro69.kfaker.Faker
import kotlinx.serialization.json.Json
import java.math.RoundingMode
import kotlin.random.Random
import android.os.Vibrator


class MainActivity1 : AppCompatActivity() {
    private var realEstates = mutableListOf<RealEstate>()
    private lateinit var binding: ActivityMain1Binding
    private lateinit var adapter: ArrayAdapter<String>
    private val faker = Faker()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)

        binding.listView.adapter = adapter
        val realEstates = (1..5).map {
            RealEstate(
                faker.company.industry(),
                Random.nextDouble(50.0, 200.0).toBigDecimal().setScale(2, RoundingMode.DOWN)
                    .toDouble(),
                Random.nextDouble(50000.0, 500000.0).toBigDecimal().setScale(2, RoundingMode.DOWN)
                    .toDouble()
            )
        }
        adapter.addAll(realEstates.map { it.toString() })

    }

    private val getDataFromMainActivity1 =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val propertyType = intent?.getStringExtra(InputTypes.PROPERTY_TYPE.name)
                val area = intent?.getDoubleExtra(InputTypes.AREA.name, 0.0)
                val price = intent?.getDoubleExtra(InputTypes.PRICE.name, 0.0)
                val realEstate = RealEstate(propertyType!!, area!!, price!!)
                adapter.add(realEstate.toString())
            }
        }

    private val getDataFromQRCode =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val scannedValue = intent?.getStringExtra("SCAN_RESULT")

                val parsedRealestate = parseJSON(scannedValue)

                if (parsedRealestate != null) {
                    Toast.makeText(this, "Got RealEstate.", Toast.LENGTH_SHORT).show()
                    vibrate()
                    adapter.add(parsedRealestate.toString())
                }
            } else {
                Toast.makeText(this, "QR Code scanning failed", Toast.LENGTH_SHORT).show()
            }
        }

    private fun parseJSON(scannedValue: String?): RealEstate? {
        return try {
            Json.decodeFromString<RealEstate>(scannedValue!!)
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to parse text.", Toast.LENGTH_SHORT).show()
            null
        }
    }

    //TODO: insert data from QR Code to the inputs in MainActivity.kt

    private fun vibrate() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) { // Vibrator availability checking
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(500) // Vibrate method for below API Level 26
            }
        }
    }

    fun onAddButtonClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        getDataFromMainActivity1.launch(intent)
    }

    fun onQRCButtonClick(view: View) {
        try {
            val intent = Intent("com.google.zxing.client.android.SCAN" )
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
            getDataFromQRCode.launch(intent)
        } catch (e: Exception) {
            val marketUri = Uri.parse("market://details?id=com.google.zxing.client.android")
            val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
            startActivity(marketIntent)
        }
    }

    fun onAboutButtonClick(view: View) {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

}