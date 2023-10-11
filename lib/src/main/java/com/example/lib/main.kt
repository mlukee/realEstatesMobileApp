package com.example.lib

import io.github.serpro69.kfaker.Faker
import java.math.RoundingMode
import kotlin.random.Random

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

    val transactions = RealEstateTransactions(realEstates).getRealEstates()

    println("Nepremicnine za stranko ${client.name} (proracun ${client.budget} EUR):")


    transactions.forEach { transaction ->
        if (transaction.price <= client.budget) {
            println(transaction)
        }
    }


}