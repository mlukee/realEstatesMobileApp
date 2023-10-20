package com.example.poraapplication

import android.R.attr.value
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lib.RealEstate
import com.example.poraapplication.databinding.ActivityMain1Binding
import com.example.poraapplication.databinding.ActivityMainBinding
import io.github.serpro69.kfaker.Faker
import java.math.RoundingMode
import kotlin.random.Random


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
        val realEstates = (1..10).map {
            RealEstate(
                faker.company.industry(),
                Random.nextDouble(50.0, 200.0).toBigDecimal().setScale(2, RoundingMode.DOWN).toDouble(),
                Random.nextDouble(50000.0, 500000.0).toBigDecimal().setScale(2, RoundingMode.DOWN)
                    .toDouble()
            )
        }
        adapter.addAll(realEstates.map { it.toString() })

    }

    private val getDataFromMainActivity1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val propertyType = intent?.getStringExtra(InputTypes.PROPERTY_TYPE.name)
            val area = intent?.getDoubleExtra(InputTypes.AREA.name, 0.0)
            val price = intent?.getDoubleExtra(InputTypes.PRICE.name, 0.0)
            val realEstate = RealEstate(propertyType!!, area!!, price!!)
            adapter.add(realEstate.toString())
        }
    }

    private val getDataFromQRCode = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            Toast.makeText(this, intent?.getStringExtra("QRCode"), Toast.LENGTH_SHORT).show()
        }
    }

    fun onAddButtonClick(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        getDataFromMainActivity1.launch(intent)
    }

    fun onQRCButtonClick(view: View) {
        try{
            val intent = Intent("com.google.zxing.client.android.SCAN")
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
            getDataFromQRCode.launch(intent)
        }catch (e: Exception){
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