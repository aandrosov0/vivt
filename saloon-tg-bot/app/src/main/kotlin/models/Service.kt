package models

enum class Service {
    MAKEUP,
    HAIR_SALON,
    BODY_AND_ART;

    companion object
}

val Service.russianName
    get() = when (this) {
        Service.MAKEUP -> "Макияж"
        Service.HAIR_SALON -> "Парикмахерская"
        Service.BODY_AND_ART -> "Боди-арт"
    }

fun Service.Companion.fromRussianName(name: String): Service {
    var currentService: Service? = null
    Service.entries.forEach { service ->
        if (name == service.russianName) {
            currentService = service
            return@forEach
        }
    }

    if (currentService == null) {
        throw IllegalArgumentException("Service with name $name cannot be found!")
    }

    return currentService
}
