package com.example.lib

import java.util.UUID

data class Client(
    val name: String,
    val email: String,
    val budget: Double,
    val id: UUID = UUID.randomUUID()
) {
}