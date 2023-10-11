package com.example.lib

import java.util.UUID

data class RealEstate(
    val propertyType: String,
    val area: Double,
    val price: Double,
    val id: UUID = UUID.randomUUID()
) : Comparable<RealEstate> {
    override fun compareTo(other: RealEstate): Int {
        return this.area.compareTo(other.area)
    }

    override fun toString(): String {
        return "RealEstate(id=$id, propertyType=$propertyType, area=$area, price=$price)"
    }
}