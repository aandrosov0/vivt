package models

enum class Quality {
    BAD,
    GOOD,
    EXCELLENT
}

val Quality.russianName
    get() = when (this) {
        Quality.BAD -> "Плохо"
        Quality.GOOD -> "Хорошо"
        Quality.EXCELLENT -> "Отлично"
    }

fun qualityFromRussianName(name: String): Quality {
    var currentQuality: Quality? = null
    Quality.entries.forEach { service ->
        if (name == service.russianName) {
            currentQuality = service
            return@forEach
        }
    }

    if (currentQuality == null) {
        throw IllegalArgumentException("Service with name $name cannot be found!")
    }

    return currentQuality!!
}
