package com.example.lib

import io.github.serpro69.kfaker.Faker
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.math.RoundingMode
import java.util.UUID
import javax.naming.Context
import kotlin.random.Random

fun serializeRealEstateList(list: List<RealEstate>): String {
    return Json.encodeToString(list)
}

fun deserializeRealEstateList(json: String): List<RealEstate> {
    return Json.decodeFromString(json)
}

fun writeToFile(data: String) {
    val file = File("real_estate_data.json")
    file.writeText(data)
}

fun readFromFile(): String {
    val file = File("real_estate_data.json")
    return file.readText()
}

fun saveRealEstate(realEstate: RealEstate) {
    val realEstateList = deserializeRealEstateList(readFromFile())
    realEstateList.toMutableList().add(realEstate)
    writeToFile(serializeRealEstateList(realEstateList))
}

fun deleteRealEstate(id: UUID) {
    val realEstateList = deserializeRealEstateList(readFromFile())
    val newList = realEstateList.filterNot { it.id == id}
    writeToFile(serializeRealEstateList(newList))
}

fun updateRealEstate(updatedRealEstate: RealEstate) {
    val realEstateList = deserializeRealEstateList(readFromFile())
    val updatedList = realEstateList.map { if (it.id == updatedRealEstate.id) updatedRealEstate else it }
    writeToFile(serializeRealEstateList(updatedList))
}



fun main() {
    val faker = Faker()

    val realEstates = (1..10).map {
        RealEstate(
            faker.company.industry(),
            Random.nextDouble(50.0, 200.0).toBigDecimal().setScale(2, RoundingMode.DOWN).toDouble(),
            Random.nextDouble(50000.0, 500000.0).toBigDecimal().setScale(2, RoundingMode.DOWN)
                .toDouble()
        )
    }

    val client = Client(
        name = faker.name.maleFirstName(),
        email = faker.internet.email(),
        250_000.00
    )

    val transactions = RealEstateTransactions().realEstates

//    println("Nepremicnine za stranko ${client.name} (proracun ${client.budget} EUR):")


//    transactions.forEach { transaction ->
//        if (transaction.price <= client.budget) {
//            println(transaction)
//        }
//    }

    val json = serializeRealEstateList(realEstates)
    writeToFile(json)
    val realEstateList = deserializeRealEstateList(readFromFile())
    println(realEstateList)
    println("=============================")
    val realEstate = realEstateList[0]
    realEstate.price = 100000.0
    updateRealEstate(realEstate)
    println(deserializeRealEstateList(readFromFile()))
    println("=============================")
    deleteRealEstate(realEstate.id)
    println(deserializeRealEstateList(readFromFile()))



}