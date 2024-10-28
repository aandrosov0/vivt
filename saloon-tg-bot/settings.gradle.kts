import java.net.URI

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = URI.create("https://jitpack.io") }
    }
}

include(":app")
