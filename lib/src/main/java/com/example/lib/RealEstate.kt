package com.example.lib

import java.util.UUID
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class RealEstate(
    val propertyType: String,
    val area: Double,
    val price: Double,
    @SerialName("id")
    @Serializable(with = UUIDSerializer::class)
    val id: UUID = UUID.randomUUID()
) : Comparable<RealEstate> {
    override fun compareTo(other: RealEstate): Int {
        return this.area.compareTo(other.area)
    }

    override fun toString(): String {
        return "Property Type: $propertyType \nArea: $area \nPrice= $price"
    }
}

@Serializer(forClass = UUID::class)
object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}