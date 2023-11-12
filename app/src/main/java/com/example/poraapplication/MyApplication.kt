package com.example.poraapplication

import android.app.Application
import com.example.lib.RealEstate
import com.example.lib.RealEstateTransactions

class MyApplication : Application() {
    var transactions: RealEstateTransactions = RealEstateTransactions()
    override fun onCreate() {
        super.onCreate()
        transactions = RealEstateTransactions()
    }
}