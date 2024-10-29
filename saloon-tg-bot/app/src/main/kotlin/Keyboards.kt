import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.keyboard.KeyboardButton
import models.Service
import models.russianName

class Keyboards {
    companion object {
        fun buildDefaultOptionsKeyboard() =
            KeyboardReplyMarkup.createSimpleKeyboard(
                listOf(listOf("Оставить отзыв", "Посмотреть отзывы")),
                resizeKeyboard = true
            )

        fun buildQualityKeyboard() =
            KeyboardReplyMarkup.createSimpleKeyboard(
                buildList {
                    for (i in 1..5) {
                        add(listOf("$i"))
                    }
                }
            )

        fun buildServicesKeyboard(services: List<Service>) =
            KeyboardReplyMarkup.createSimpleKeyboard(
                buildList {
                    services.forEach { service -> add(listOf(service.russianName)) }
                }
            )

        fun buildContactKeyboard() =
            KeyboardReplyMarkup(
                listOf(listOf(KeyboardButton("Прислать номер", requestContact = true))),
                resizeKeyboard = true
            )

        fun generateKeyboard(rows: List<String>) =
            KeyboardReplyMarkup.createSimpleKeyboard(
                buildList { rows.forEach { add(listOf(it)) } }
            )
    }
}
