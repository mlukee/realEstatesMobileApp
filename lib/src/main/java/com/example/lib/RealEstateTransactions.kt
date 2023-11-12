package com.example.lib

class RealEstateTransactions() {
    val realEstates: MutableList<RealEstate> = mutableListOf()

    fun addRealEstate(realEstate: RealEstate) {
        realEstates.add(realEstate)
    }

    fun getRealEstateSize(): Int {
        return realEstates.size
    }

    fun removeRealEstate(realEstate: RealEstate) {
        realEstates.remove(realEstate)
    }

    fun addRealEstates(realEstates: List<RealEstate>) {
        this.realEstates.addAll(realEstates)
    }

}