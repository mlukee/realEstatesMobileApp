plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    kotlin("plugin.serialization") version "1.9.0"

}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation ("io.github.serpro69:kotlin-faker:1.15.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

}