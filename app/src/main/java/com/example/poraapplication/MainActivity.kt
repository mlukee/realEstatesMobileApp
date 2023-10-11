package com.example.poraapplication

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.lib.RealEstate
import com.example.poraapplication.databinding.ActivityMainBinding
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var realEstates = mutableListOf<RealEstate>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun onButtonInfoClick(view: View){
        Log.i(MainActivity::class.simpleName, "Number of elements in list: ${realEstates.size}")
    }

    fun onButtonAddClick(view: View) {
        val propertyType = binding.editTextPropertyType.text.toString()
        val areaText = binding.editTextArea.text.toString()
        val priceText = binding.editTextPrice.text.toString()
        val alertDialogBuilder = AlertDialog.Builder(this)
        try {
            if (propertyType.isEmpty() || areaText.isEmpty() || priceText.isEmpty()) {
                throw IllegalArgumentException("All fields are required")
            }

            val area = areaText.toDouble()
            val price = priceText.toDouble()

            println("$propertyType\t$area\t$price")
            val estate = RealEstate(propertyType, area, price)


            if(realEstates.add(estate)) {
                alertDialogBuilder.setMessage("Successfully added: ${estate.propertyType}, ${estate.area}, ${estate.price}")
                    .setPositiveButton("OK") { dialogInterface,i -> dialogInterface.dismiss()} .show()
                binding.editTextPropertyType.text.clear()
                binding.editTextArea.text.clear()
                binding.editTextPrice.text.clear()
            }


        } catch (e: IllegalArgumentException) {
            alertDialogBuilder.setMessage(e.message).setPositiveButton("OK"
            ) { dialogInterface, i -> dialogInterface.dismiss() }
            alertDialogBuilder.show()
        }
    }

    fun onButtonExitClick(view: View){
        MainActivity().finish()
        exitProcess(0)
    }
}