package com.example.poraapplication

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import com.example.lib.RealEstate
import com.example.lib.RealEstateTransactions
import com.example.lib.deserializeRealEstateList
import java.io.File
import java.math.RoundingMode
import java.util.UUID
import kotlin.random.Random
import io.github.serpro69.kfaker.faker

const val FILE_NAME = "real_estate_data.json"

class MyApplication : Application() {
    var transactions: RealEstateTransactions = RealEstateTransactions()
    private lateinit var uuid: String
    lateinit var file: File

    companion object {
        const val PREFS_NAME = "AppAnalyticsPrefs"
        private const val KEY_APP_OPENS = "app_opens"
        private const val KEY_APP_BACKGROUND = "app_background"

        fun incrementAppOpenCount(context: Context) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            val opens = sharedPreferences.getInt(KEY_APP_OPENS, 0)
            sharedPreferences.edit().putInt(KEY_APP_OPENS, opens + 1).apply()
        }

        fun incrementAppBackgroundCount(context: Context) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            val background = sharedPreferences.getInt(KEY_APP_BACKGROUND, 0)
            sharedPreferences.edit().putInt(KEY_APP_BACKGROUND, background + 1).apply()
        }
    }
    override fun onCreate() {
        super.onCreate()
        incrementAppOpenCount(this)
        transactions = RealEstateTransactions()
        file = File(filesDir, FILE_NAME)
        initData()

        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        if (!sharedPreferences.contains("uuid")) {
            uuid = UUID.randomUUID().toString()
            with(sharedPreferences.edit()) {
                putString("uuid", uuid)
                apply()
            }
        } else {
            uuid = sharedPreferences.getString("uuid", "")!!
        }
//        val realEstates = (1..100).map {
//            RealEstate(
//                faker {  }.company.industry(),
//                Random.nextDouble(50.0, 200.0).toBigDecimal().setScale(2, RoundingMode.DOWN)
//                    .toDouble(),
//                Random.nextDouble(50000.0, 500000.0).toBigDecimal().setScale(2, RoundingMode.DOWN)
//                    .toDouble()
//            )
//        }
//        transactions.addRealEstates(realEstates)
    }

    private fun initData(){
        if(file.exists()){
            val json = file.readText()
            transactions.realEstates = deserializeRealEstateList(json).toMutableList()
        }else{
            val realEstates = (1..100).map {
                RealEstate(
                    faker {}.company.industry(),
                    Random.nextDouble(50.0, 200.0).toBigDecimal().setScale(2, RoundingMode.DOWN).toDouble(),
                    Random.nextDouble(50000.0, 500000.0).toBigDecimal().setScale(2, RoundingMode.DOWN)
                        .toDouble()
                )
            }
            transactions.addRealEstates(realEstates)
            file.writeText(transactions.serializeRealEstateList())
        }

    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            // App went to the background
            incrementAppBackgroundCount(this)
        }
    }


}