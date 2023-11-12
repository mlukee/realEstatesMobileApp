package com.example.poraapplication

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import com.example.lib.RealEstate
import com.example.lib.RealEstateTransactions
import java.util.UUID

class MyApplication : Application() {
    var transactions: RealEstateTransactions = RealEstateTransactions()
    private lateinit var uuid: String

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
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            // App went to the background
            incrementAppBackgroundCount(this)
        }
    }
}