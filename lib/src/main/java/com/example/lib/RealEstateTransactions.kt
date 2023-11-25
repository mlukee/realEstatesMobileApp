package com.example.lib

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RealEstateTransactions() {
    var realEstates: MutableList<RealEstate> = mutableListOf()

    fun addRealEstate(realEstate: RealEstate) {
        realEstates.add(realEstate)
    }

    fun getRealEstateSize(): Int {
        return realEstates.size
    }

    fun removeRealEstate(realEstate: RealEstate) {
        realEstates.remove(realEstate)

    }

    //update realestate
    fun updateRealEstate(realEstate: RealEstate) {
        val index = realEstates.indexOf(realEstate)
        if (index != -1) {
            realEstates[index] = realEstate
        }
    }

    fun addRealEstates(realEstates: List<RealEstate>) {
        this.realEstates.addAll(realEstates)
    }

    fun serializeRealEstateList(): String {
        return Json.encodeToString(realEstates)
    }

    fun deserializeRealEstateList(json: String): List<RealEstate> {
        return Json.decodeFromString(json)
    }

}