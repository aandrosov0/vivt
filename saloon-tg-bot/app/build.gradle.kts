plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}

application {
    mainClass = "ApplicationKt"
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.slf4j.simple)
    implementation(libs.kotlin.telegram.bot)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.exposed.core)
    implementation(libs.exposed.jdbc)
    implementation("org.xerial:sqlite-jdbc:3.46.1.0")
}

